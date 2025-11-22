package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO;
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO;
import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerAppointmentRequest;
import com.group02.ev_maintenancesystem.dto.request.ServiceItemApproveRequest;
import com.group02.ev_maintenancesystem.dto.request.ServiceItemUpgradeRequest;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.dto.response.AppointmentServiceItemDetailResponse;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import com.group02.ev_maintenancesystem.enums.Role;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.AppointmentMapper;
import com.group02.ev_maintenancesystem.mapper.AppointmentServiceItemDetailMapper;
import com.group02.ev_maintenancesystem.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentServiceImpl implements AppointmentService {

    AppointmentRepository appointmentRepository;
    UserRepository userRepository;
    AppointmentMapper appointmentMapper;
    VehicleRepository vehicleRepository;
    ServiceItemRepository serviceItemRepository;
    ModelPackageItemRepository modelPackageItemRepository;
    MaintenanceRecordService maintenanceRecordService;
    MaintenanceService maintenanceService;
    ServiceCenterRepository serviceCenterRepository;
    AppointmentServiceItemDetailRepository appointmentServiceItemDetailRepository;
    AppointmentServiceItemDetailMapper appointmentServiceItemDetailMapper;
    EmailService emailService;

    @Override
    @Transactional
    public AppointmentResponse createAppointmentByCustomer(Authentication authentication, CustomerAppointmentRequest request) {
        // --- VALIDATION GIỜ HÀNH CHÍNH (Yêu cầu 3) ---
        LocalTime appointmentTime = request.getAppointmentDate().toLocalTime();
        LocalTime startTime = LocalTime.of(7, 0); // 7:00 AM
        LocalTime endTime = LocalTime.of(18, 0); // 6:00 PM

        if (appointmentTime.isBefore(startTime) || appointmentTime.isAfter(endTime)) {
            throw new AppException(ErrorCode.APPOINTMENT_TIME_OUT_OF_BOUNDS);
        }
        // --- KẾT THÚC VALIDATION ---

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long customerId = jwt.getClaim("userId");
        User customer = userRepository.findByIdAndRole(customerId, Role.CUSTOMER)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        if (!vehicle.getCustomerUser().getId().equals(customerId)) {
            throw new AppException(ErrorCode.VEHICLE_NOT_BELONG_TO_CUSTOMER);
        }
        ServiceCenter serviceCenter = serviceCenterRepository.findById(request.getCenterId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CENTER_NOT_FOUND));

        // --- LOGIC MỚI (ĐÃ THAY THẾ): Kiểm tra xe có lịch hẹn đang "active" hay không ---
        // Lấy TẤT CẢ các lịch hẹn của xe này, không phân biệt ngày
        List<Appointment> allAppointmentsForVehicle = appointmentRepository.findByVehicleIdOrderByCreatedAtDesc(request.getVehicleId());

        // Kiểm tra xem có BẤT KỲ lịch hẹn nào đang "active" (không phải CANCELLED hoặc COMPLETED)
        boolean vehicleHasActiveAppointment = allAppointmentsForVehicle.stream()
                .anyMatch(app -> app.getStatus() != AppointmentStatus.CANCELLED
                        && app.getStatus() != AppointmentStatus.COMPLETED);

        if (vehicleHasActiveAppointment) {
            // Nếu xe đang có 1 lịch PENDING, CONFIRMED, hoặc WAITING_FOR_APPROVAL, không cho đặt lịch mới.
            log.warn("Customer {} tried to book for vehicle {} which already has an active appointment.", customerId, request.getVehicleId());
            // (Bạn có thể tạo ErrorCode "VEHICLE_HAS_ACTIVE_APPOINTMENT" nếu muốn rõ nghĩa hơn)
            throw new AppException(ErrorCode.APPOINTMENT_ALREADY_EXISTS);
        }
        // --- KẾT THÚC LOGIC MỚI ---

        List<MaintenanceRecommendationDTO> recommendations = maintenanceService.getRecommendations(vehicle.getId());
        if (recommendations.isEmpty()) {
            // Cải tiến: Ném ra lỗi cụ thể thay vì UNCATEGORIZED
            log.warn("No maintenance recommendations found for vehicle {}. Cannot create appointment.", vehicle.getId());
            throw new AppException(ErrorCode.NO_MAINTENANCE_DUE); //
        }
        MaintenanceRecommendationDTO recommendation = recommendations.get(0);
        Integer recommendedMilestoneKm = recommendation.getMilestoneKm();

        String milestonePackageName = "Maintenance " + recommendedMilestoneKm + "km milestone";

        Appointment appointment = new Appointment();
        appointment.setCustomerUser(customer);
        appointment.setVehicle(vehicle);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setEstimatedCost(recommendation.getEstimatedTotal());
        appointment.setServicePackageName(milestonePackageName);
        appointment.setServiceCenter(serviceCenter);
        appointment.setMilestoneKm(recommendedMilestoneKm);

        if (recommendation.getItems() == null || recommendation.getItems().isEmpty()) {
            log.warn("Recommendation for milestone {}km has no service items listed.", recommendedMilestoneKm);
            throw new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND);
        }

        for (ModelPackageItemDTO itemDto : recommendation.getItems()) {
            ServiceItem serviceItem = serviceItemRepository.findById(itemDto.getServiceItem().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND));

            AppointmentServiceItemDetail detail = AppointmentServiceItemDetail.builder()
                    .appointment(appointment)
                    .serviceItem(serviceItem)
                    .actionType(itemDto.getActionType())
                    .price(itemDto.getPrice())
                    .customerApproved(true)
                    .build();

            appointment.addServiceDetail(detail);
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        appointmentRepository.flush();
        log.info("Saving customer success, now sending email...");
        try {
            log.info(">>> SENDING EMAIL...");
            emailService.sendAppointmentConfirmation(savedAppointment);
            log.info(">>> EMAIL SENT SUCCESS");
        } catch (Exception e) {
            log.error(">>> EMAIL SEND FAILED: {}", e.getMessage(), e);
        }

        AppointmentResponse response = mapSingleAppointmentToResponse(savedAppointment);
        log.info("Successfully created appointment ID {} for vehicle ID {} at milestone {}km.", savedAppointment.getId(), vehicle.getId(), recommendedMilestoneKm);
        return response;
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public List<AppointmentResponse> getAppointmentByCustomerId(Long customerId) {
        userRepository.findByIdAndRole(customerId, Role.CUSTOMER)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByCustomerUserIdOrderByCreatedAtDesc(customerId);
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    public List<AppointmentResponse> getAppointmentByVehicleId(Long vehicleId) {
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByVehicleIdOrderByCreatedAtDesc(vehicleId);
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    public AppointmentResponse getAppointmentByAppointmentId(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        return mapSingleAppointmentToResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> getAppointmentByStatus(AppointmentStatus status, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        List<Appointment> appointments;
        if (user.isAdmin()) {
            appointments = appointmentRepository.findByStatusOrderByCreatedAtDesc(status);
        } else if (user.isStaff() || user.isTechnician()) {
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            appointments = appointmentRepository.findByStatusAndServiceCenterIdOrderByCreatedAtDesc(status, centerId);
        } else {
            return Collections.emptyList();
        }
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    public List<AppointmentResponse> getAppointmentByTechnicianId(Long technicianId) {
        userRepository.findByIdAndRole(technicianId, Role.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByTechnicianUserIdOrderByCreatedAtDesc(technicianId);
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    @Transactional
    public AppointmentResponse assignTechnician(Long appointmentId, Long technicianId, Authentication authentication) {
        User staff = getAuthenticatedUser(authentication);
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        User technician = userRepository.findByIdAndRole(technicianId, Role.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (staff.isStaff() && (appointment.getServiceCenter() == null ||
                !appointment.getServiceCenter().getId().equals(staff.getServiceCenter().getId()))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (appointment.getServiceCenter() != null && technician.getServiceCenter() != null) {
            if (!appointment.getServiceCenter().getId().equals(technician.getServiceCenter().getId())) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }
        appointment.setTechnicianUser(technician);
        boolean statusChanged = false;
        if (appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            statusChanged = true;
        }
        Appointment savedAppointment = appointmentRepository.save(appointment);
        appointmentRepository.flush();

        // Chỉ gửi email xác nhận 1 LẦN DUY NHẤT tại đây
        if (statusChanged) {
            try {
                emailService.sendConfirmAndAssignAppointment(savedAppointment);
            } catch (Exception e) {
                log.error("Failed to send confirmation email for appointment {}: {}", savedAppointment.getId(), e.getMessage());
            }
        }
        return mapSingleAppointmentToResponse(savedAppointment);
    }

    @Override
    public List<AppointmentResponse> getAll(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        List<Appointment> appointments;
        if (user.isAdmin()) {
            appointments = appointmentRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else if (user.isStaff() || user.isTechnician()) {
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            appointments = appointmentRepository.findAllByServiceCenterIdOrderByCreatedAtDesc(centerId);
        } else {
            return Collections.emptyList();
        }
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    public List<AppointmentResponse> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate, Authentication authentication) {
        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        User user = getAuthenticatedUser(authentication);
        List<Appointment> appointments;
        if (user.isAdmin()) {
            appointments = appointmentRepository.findByAppointmentDateBetweenOrderByCreatedAtDesc(startDate, endDate);
        } else if (user.isStaff() || user.isTechnician()) {
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            appointments = appointmentRepository.findByAppointmentDateBetweenAndServiceCenterIdOrderByCreatedAtDesc(startDate, endDate, centerId);
        } else {
            return Collections.emptyList();
        }
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    @Transactional
    public AppointmentResponse setStatusAppointment(Long id, AppointmentStatus newStatus, Authentication authentication) {
        if (newStatus == null) {
            throw new AppException(ErrorCode.STATUS_INVALID);
        }
        User user = getAuthenticatedUser(authentication);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        checkAppointmentAccess(user, appointment);

        // --- BẮT ĐẦU SỬA: CẬP NHẬT LOGIC CHO IN_PROGRESS ---
        if (newStatus == AppointmentStatus.IN_PROGRESS) {
            if (!user.isTechnician()) {
                throw new AppException(ErrorCode.UNAUTHORIZED); // Chỉ tech mới được IN_PROGRESS
            }
            // Tech có thể bắt đầu làm việc nếu lịch đã CONFIRMED hoặc KH đã duyệt
            if (appointment.getStatus() != AppointmentStatus.CONFIRMED &&
                    appointment.getStatus() != AppointmentStatus.CUSTOMER_APPROVED) { // <-- Sửa: Bỏ REJECTED

                log.warn("Technician {} tried to set IN_PROGRESS on appointment {} with status {}", user.getUsername(), id, appointment.getStatus());
                throw new AppException(ErrorCode.STATUS_INVALID);
            }
        }
        // --- KẾT THÚC SỬA ---

        if (appointment.getStatus() == AppointmentStatus.WAITING_FOR_APPROVAL &&
                newStatus != AppointmentStatus.CANCELLED) {
            if (user.isTechnician()) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            if (newStatus == AppointmentStatus.COMPLETED) {
                boolean allApproved = appointment.getServiceDetails().stream()
                        .allMatch(AppointmentServiceItemDetail::getCustomerApproved);
                if (!allApproved) {
                    throw new AppException(ErrorCode.SERVICE_ITEM_NOT_APPROVED_YET);
                }
            }
        }

        if (newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.IN_PROGRESS) {
            // Không cho phép bắt đầu hoặc hoàn thành nếu chưa đến giờ hẹn
            // Có thể cho phép sai số nhỏ (ví dụ: đến sớm 30 phút) nếu muốn, tùy nghiệp vụ
            if (LocalDateTime.now().isBefore(appointment.getAppointmentDate())) {
                log.warn("Attempt to process future appointment ID {} too early.", id);
                // Bạn nên định nghĩa thêm ErrorCode.APPOINTMENT_NOT_STARTED_YET
                throw new AppException(ErrorCode.APPOINTMENT_TIME_NOT_ARRIVED);
            }
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED || appointment.getStatus() == AppointmentStatus.CANCELLED) {
            if (appointment.getStatus() != newStatus) {
                log.warn("Attempted to change status of already {} appointment ID {}", appointment.getStatus(), id);
                return mapSingleAppointmentToResponse(appointment);
            }
        }
        if (newStatus == AppointmentStatus.COMPLETED && appointment.getTechnicianUser() == null) {
            throw new AppException(ErrorCode.TECHNICIAN_NOT_ASSIGNED);
        }

        appointment.setStatus(newStatus);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        if (newStatus == AppointmentStatus.COMPLETED) {
            maintenanceRecordService.createMaintenanceRecord(savedAppointment);
        }
        return mapSingleAppointmentToResponse(savedAppointment);
    }

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentUpdateRequest appointment) {
        throw new UnsupportedOperationException("Use assignTechnician or setStatusAppointment instead.");
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(Long appointmentId, Authentication authentication) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        User currentUser = getAuthenticatedUser(authentication);

        boolean isAdmin = currentUser.isAdmin();
        boolean isStaffOfCenter = currentUser.isStaff() &&
                currentUser.getServiceCenter() != null &&
                appointment.getServiceCenter() != null &&
                currentUser.getServiceCenter().getId().equals(appointment.getServiceCenter().getId());
        boolean isOwnerCustomer = currentUser.isCustomer() &&
                appointment.getCustomerUser().getId().equals(currentUser.getId());
        boolean isAssignedTechnician = currentUser.isTechnician() &&
                appointment.getTechnicianUser() != null &&
                appointment.getTechnicianUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isStaffOfCenter && !isOwnerCustomer && !isAssignedTechnician) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppException(ErrorCode.CANNOT_CANCEL_COMPLETED_APPOINTMENT);
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            return mapSingleAppointmentToResponse(appointment);
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapSingleAppointmentToResponse(savedAppointment);
    }

    // --- SỬA LỖI BUG (Yêu cầu 4) ---
    @Override
    @Transactional
    public AppointmentResponse upgradeServiceItem(Long appointmentId, List<ServiceItemUpgradeRequest> requests, Authentication authentication) {
        User technician = getAuthenticatedUser(authentication);
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // 1. Kiểm tra quyền và trạng thái (chỉ cần 1 lần)
        if (!technician.isTechnician() || appointment.getTechnicianUser() == null || !appointment.getTechnicianUser().getId().equals(technician.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        // --- BẮT ĐẦU SỬA: Cho phép KTV nâng cấp khi đang CONFIRMED, IN_PROGRESS hoặc CUSTOMER_APPROVED ---
        // (KTV có thể phát hiện thêm khi đang làm)
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED &&
                appointment.getStatus() != AppointmentStatus.IN_PROGRESS &&
                appointment.getStatus() != AppointmentStatus.CUSTOMER_APPROVED) { // <-- Sửa
            log.warn("Technician {} tried to upgrade items on appointment {} with status {}", technician.getUsername(), appointmentId, appointment.getStatus());
            throw new AppException(ErrorCode.STATUS_INVALID);
        }
        // --- KẾT THÚC SỬA ---

        if (requests == null || requests.isEmpty()) {
            log.warn("upgradeServiceItem called with no requests for appointment {}", appointmentId);
            return mapSingleAppointmentToResponse(appointment);
        }

        // Lấy danh sách giá nâng cấp (REPLACE) một lần
        List<ModelPackageItem> replacePriceDefinitions = modelPackageItemRepository
                .findAllByVehicleModelIdOrderByCreatedAtDesc(appointment.getVehicle().getModel().getId())
                .stream()
                .filter(item -> item.getActionType() == MaintenanceActionType.REPLACE)
                .toList();

        // 2. Bắt đầu vòng lặp
        for (ServiceItemUpgradeRequest request : requests) {

            // Tìm chi tiết hạng mục trong lịch hẹn
            AppointmentServiceItemDetail detail = appointmentServiceItemDetailRepository.findByAppointmentIdAndServiceItemId(appointmentId, request.getServiceItemId())
                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_IN_APPOINTMENT));

            if (detail.getActionType() == request.getNewActionType()) {
                // Nếu đã là REPLACE, chỉ cập nhật ghi chú
                detail.setTechnicianNotes(request.getNotes());
                appointmentServiceItemDetailRepository.save(detail); // (Lưu tạm ghi chú)
                continue; // Chuyển sang request tiếp theo
            }
            if (detail.getActionType() != MaintenanceActionType.CHECK || request.getNewActionType() != MaintenanceActionType.REPLACE) {
                // Chỉ cho phép nâng cấp từ CHECK -> REPLACE
                throw new AppException(ErrorCode.ONLY_UPGRADE_CHECK_TO_REPLACE_ALLOWED);
            }

            // Tìm giá REPLACE cho hạng mục này
            Optional<ModelPackageItem> replacePriceOpt = replacePriceDefinitions.stream()
                    .filter(item -> item.getServiceItem().getId().equals(request.getServiceItemId()))
                    .min(Comparator.comparing(ModelPackageItem::getMilestoneKm)); // Ưu tiên giá ở mốc thấp nhất

            if (replacePriceOpt.isEmpty()) {
                log.error("No 'REPLACE' price definition found for item {} for model {} in ANY milestone",
                        request.getServiceItemId(), appointment.getVehicle().getModel().getId());
                throw new AppException(ErrorCode.SERVICE_ITEM_PRICE_NOT_FOUND);
            }

            BigDecimal newPrice = replacePriceOpt.get().getPrice();

            // Cập nhật chi tiết hạng mục
            detail.setActionType(request.getNewActionType());
            detail.setPrice(newPrice);
            detail.setCustomerApproved(false); // Chuyển sang chờ duyệt
            detail.setTechnicianNotes(request.getNotes());
            // (Không save ở đây, để save 1 lần ở cuối)
        }

        // 3. Cập nhật lịch hẹn (sau khi vòng lặp kết thúc)
        appointment.setStatus(AppointmentStatus.WAITING_FOR_APPROVAL); // Đặt trạng thái chờ duyệt
        appointment.recalculateEstimatedCost(); // Tính lại tổng tiền dựa trên các mục đã duyệt

        Appointment savedAppointment = appointmentRepository.save(appointment); // Lưu 1 lần
        log.info("Technician {} upgraded {} item(s) for appointment {}", technician.getUsername(), requests.size(), appointmentId);

        return mapSingleAppointmentToResponse(savedAppointment);
    }

    @Override
    @Transactional
    public AppointmentResponse approveServiceItem(Long appointmentId, List<ServiceItemApproveRequest> requests, Authentication authentication) {
        User staff = getAuthenticatedUser(authentication);
        if (!staff.isAdmin() && !staff.isStaff()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        checkAppointmentAccess(staff, appointment);

        if (requests == null || requests.isEmpty()) {
            log.warn("approveServiceItem called with no requests for appointment {}", appointmentId);
            return mapSingleAppointmentToResponse(appointment);
        }

        // --- BẮT ĐẦU SỬA: LOGIC XỬ LÝ 1 DUYỆT, 1 TỪ CHỐI ---

        int totalUpgradeRequestsInBatch = requests.size();
        int totalRejectionsInBatch = 0;

        for (ServiceItemApproveRequest request : requests) {
            AppointmentServiceItemDetail detail = appointmentServiceItemDetailRepository.findById(request.getAppointmentServiceDetailId())
                    .orElseThrow(() -> new AppException(ErrorCode.DETAIL_NOT_FOUND));

            if (!detail.getAppointment().getId().equals(appointmentId)) {
                log.error("Staff {} tried to approve detail {} which does not belong to appointment {}", staff.getUsername(), detail.getId(), appointmentId);
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            // Chỉ xử lý các item đang chờ duyệt (approved=false)
            if (detail.getCustomerApproved()) {
                // Nếu item đã được duyệt, nó không phải là 1 "request mới" cần xử lý
                totalUpgradeRequestsInBatch--;
                continue;
            }

            if (request.getApproved()) {
                // Khách hàng ĐỒNG Ý nâng cấp (từ false -> true)
                detail.setCustomerApproved(true);
            } else {
                // Khách hàng TỪ CHỐI (từ false -> revert về CHECK)
                totalRejectionsInBatch++; // Đếm 1 lần từ chối
                log.warn("Staff reverting item {} for appointment {} back to CHECK", detail.getServiceItem().getId(), appointmentId);

                ModelPackageItem originalItem = modelPackageItemRepository
                        .findByVehicleModelIdAndMilestoneKmAndServiceItemId(
                                appointment.getVehicle().getModel().getId(),
                                appointment.getMilestoneKm(),
                                detail.getServiceItem().getId()
                        )
                        .filter(item -> item.getActionType() == MaintenanceActionType.CHECK)
                        .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_CANNOT_BE_REVERTED));

                detail.setActionType(originalItem.getActionType());
                detail.setPrice(originalItem.getPrice());
                detail.setCustomerApproved(true); // Đã duyệt (với tư cách là CHECK)
            }
        }

        appointment.recalculateEstimatedCost(); // Tính lại tổng tiền

        // Kiểm tra xem tất cả các hạng mục (sau khi cập nhật) đã được duyệt chưa
        boolean allItemsInAppointmentAreHandled = appointment.getServiceDetails().stream()
                .allMatch(AppointmentServiceItemDetail::getCustomerApproved);

        // Logic mới để xử lý các kịch bản
        if (allItemsInAppointmentAreHandled) {
            // Nếu không còn gì chờ duyệt

            // --- SỬA ĐỔI THEO YÊU CẦU MỚI ---
            if (totalRejectionsInBatch > 0 && totalRejectionsInBatch == totalUpgradeRequestsInBatch) {
                // Kịch bản 1 (Yêu cầu 4): TẤT CẢ yêu cầu trong đợt này bị từ chối
                appointment.setStatus(AppointmentStatus.COMPLETED); // <-- SỬA TỪ CONFIRMED SANG COMPLETED
                // Vì đã COMPLETED, chúng ta phải tạo Maintenance Record ngay lập tức
                maintenanceRecordService.createMaintenanceRecord(appointment); // <-- THÊM MỚI
            } else {
                // Kịch bản 2 (Yêu cầu 3): Có ít nhất 1 duyệt (hoặc 1 duyệt 1 từ chối)
                appointment.setStatus(AppointmentStatus.CUSTOMER_APPROVED);
            }
            // --- KẾT THÚC SỬA ĐỔI ---

        } else {
            // Nếu vẫn còn mục đang chờ (false), trạng thái giữ nguyên
            appointment.setStatus(AppointmentStatus.WAITING_FOR_APPROVAL);
        }
        // --- KẾT THÚC SỬA ---

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Staff {} set approval for {} item(s) for appointment {}", staff.getUsername(), requests.size(), appointmentId);

        // (Không gửi email ở đây)

        return mapSingleAppointmentToResponse(savedAppointment);
    }


    private List<AppointmentResponse> mapAppointmentListToResponse(List<Appointment> appointments) {
        if (appointments == null || appointments.isEmpty()) {
            return Collections.emptyList();
        }
        return appointments.stream()
                .map(this::mapSingleAppointmentToResponse)
                .collect(Collectors.toList());
    }

    private AppointmentResponse mapSingleAppointmentToResponse(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        AppointmentResponse response = appointmentMapper.toAppointmentResponse(appointment);
        if (appointment.getVehicle() == null || appointment.getVehicle().getModel() == null || appointment.getServiceDetails() == null) {
            response.setServiceItems(Collections.emptyList());
            return response;
        }

        List<ModelPackageItemDTO> modelPackageItemDTOs = new ArrayList<>();
        for (AppointmentServiceItemDetail detail : appointment.getServiceDetails()) {
            ServiceItem item = detail.getServiceItem();
            if (item == null) continue;
            ServiceItemDTO nestedItemDTO = ServiceItemDTO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .build();
            ModelPackageItemDTO outerItemDTO = new ModelPackageItemDTO(
                    nestedItemDTO,
                    detail.getPrice(),
                    detail.getActionType()
            );
            modelPackageItemDTOs.add(outerItemDTO);
        }
        response.setServiceItems(modelPackageItemDTOs);
        return response;
    }

//    @Override
//    @Transactional
//    public AppointmentResponse approveServiceItem(Long appointmentId, List<ServiceItemApproveRequest> requests, Authentication authentication) {
//        User staff = getAuthenticatedUser(authentication);
//        if (!staff.isAdmin() && !staff.isStaff()) {
//            throw new AppException(ErrorCode.UNAUTHORIZED);
//        }
//
//        Appointment appointment = appointmentRepository.findById(appointmentId)
//                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
//        checkAppointmentAccess(staff, appointment);
//
//        if (requests == null || requests.isEmpty()) {
//            log.warn("approveServiceItem called with no requests for appointment {}", appointmentId);
//            return mapSingleAppointmentToResponse(appointment);
//        }
//
//        int totalUpgradeRequestsInBatch = requests.size();
//        int totalRejectionsInBatch = 0;
//
//        for (ServiceItemApproveRequest request : requests) {
//            AppointmentServiceItemDetail detail = appointmentServiceItemDetailRepository.findById(request.getAppointmentServiceDetailId())
//                    .orElseThrow(() -> new AppException(ErrorCode.DETAIL_NOT_FOUND));
//
//            if (!detail.getAppointment().getId().equals(appointmentId)) {
//                log.error("Staff {} tried to approve detail {} which does not belong to appointment {}", staff.getUsername(), detail.getId(), appointmentId);
//                throw new AppException(ErrorCode.UNAUTHORIZED);
//            }
//
//            if (detail.getCustomerApproved()) {
//                totalUpgradeRequestsInBatch--;
//                continue;
//            }
//
//            if (request.getApproved()) {
//                detail.setCustomerApproved(true);
//            } else {
//                totalRejectionsInBatch++;
//                log.warn("Staff reverting item {} for appointment {} back to CHECK", detail.getServiceItem().getId(), appointmentId);
//
//                ModelPackageItem originalItem = modelPackageItemRepository
//                        .findByVehicleModelIdAndMilestoneKmAndServiceItemId(
//                                appointment.getVehicle().getModel().getId(),
//                                appointment.getMilestoneKm(),
//                                detail.getServiceItem().getId()
//                        )
//                        .filter(item -> item.getActionType() == MaintenanceActionType.CHECK)
//                        .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_CANNOT_BE_REVERTED));
//
//                detail.setActionType(originalItem.getActionType());
//                detail.setPrice(originalItem.getPrice());
//                detail.setCustomerApproved(true);
//            }
//        }
//
//        appointment.recalculateEstimatedCost();
//
//        boolean allItemsInAppointmentAreHandled = appointment.getServiceDetails().stream()
//                .allMatch(AppointmentServiceItemDetail::getCustomerApproved);
//
//        // --- BẮT ĐẦU SỬA LỖI ---
//
//        boolean createRecordFlag = false; // 1. Đặt một cờ (flag)
//
//        if (allItemsInAppointmentAreHandled) {
//            if (totalRejectionsInBatch > 0 && totalRejectionsInBatch == totalUpgradeRequestsInBatch) {
//                // Kịch bản 1: TẤT CẢ bị từ chối -> COMPLETED
//                appointment.setStatus(AppointmentStatus.COMPLETED);
//                createRecordFlag = true; // 2. Đánh dấu cờ để tạo record
//            } else {
//                // Kịch bản 2: Có ít nhất 1 duyệt -> CUSTOMER_APPROVED
//                appointment.setStatus(AppointmentStatus.CUSTOMER_APPROVED);
//            }
//        } else {
//            // Kịch bản 3: Vẫn còn item đang chờ (false)
//            appointment.setStatus(AppointmentStatus.WAITING_FOR_APPROVAL);
//        }
//
//        // 3. LƯU APPOINTMENT (VỚI STATUS MỚI) TRƯỚC TIÊN
//        // Hành động này làm cho 'appointment' trở thành "persistent"
//        Appointment savedAppointment = appointmentRepository.save(appointment);
//
//        log.info("Staff {} set approval for {} item(s) for appointment {}. New status: {}",
//                staff.getUsername(), requests.size(), appointmentId, savedAppointment.getStatus());
//
//        // 4. TẠO MAINTENANCE RECORD (NẾU CỜ ĐƯỢC ĐÁNH DẤU)
//        // Chúng ta gọi hàm này SAU KHI đã save và dùng đối tượng 'savedAppointment'
//        if (createRecordFlag) {
//            maintenanceRecordService.createMaintenanceRecord(savedAppointment);
//        }
//        // --- KẾT THÚC SỬA LỖI ---
//
//        return mapSingleAppointmentToResponse(savedAppointment);
//    }

    private void checkAppointmentAccess(User user, Appointment appointment) {
        if (user.isAdmin()) {
            return;
        }
        if (user.isStaff() && user.getServiceCenter() != null &&
                appointment.getServiceCenter() != null &&
                user.getServiceCenter().getId().equals(appointment.getServiceCenter().getId())) {
            return;
        }
        if (user.isTechnician() && appointment.getTechnicianUser() != null &&
                appointment.getTechnicianUser().getId().equals(user.getId())) {
            return;
        }
        throw new AppException(ErrorCode.UNAUTHORIZED);
    }
    @Override
    public List<AppointmentServiceItemDetailResponse> getAppointmentDetails(Long appointmentId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // Kiểm tra quyền xem (chủ xe, staff, admin, hoặc KTV được gán)
        checkAppointmentAccess(user, appointment);

        // Lấy danh sách chi tiết từ lịch hẹn
        List<AppointmentServiceItemDetail> details = appointment.getServiceDetails();

        if (details == null || details.isEmpty()) {
            return Collections.emptyList();
        }

        // Map sang DTO
        return details.stream()
                .map(appointmentServiceItemDetailMapper::toResponse)
                .collect(Collectors.toList());
    }
}