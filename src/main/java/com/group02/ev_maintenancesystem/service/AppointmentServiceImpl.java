package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO;
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO;
import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerAppointmentRequest;
import com.group02.ev_maintenancesystem.dto.request.ServiceItemApproveRequest;
import com.group02.ev_maintenancesystem.dto.request.ServiceItemUpgradeRequest;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.AppointmentMapper;
import com.group02.ev_maintenancesystem.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    ServicePackageRepository servicePackageRepository;
    ServiceItemRepository serviceItemRepository;
    ModelPackageItemRepository modelPackageItemRepository;
    MaintenanceRecordService maintenanceRecordService;
    MaintenanceService maintenanceService;
    ServiceCenterRepository serviceCenterRepository;
    AppointmentServiceItemDetailRepository appointmentServiceItemDetailRepository;

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
        User customer = userRepository.findByIdAndRoleName(customerId, PredefinedRole.CUSTOMER)
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
        List<Appointment> allAppointmentsForVehicle = appointmentRepository.findByVehicleId(request.getVehicleId());

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
        ServicePackage servicePackageForMilestone = servicePackageRepository.findByName(milestonePackageName)
                .orElseGet(() -> {
                    log.warn("ServicePackage with name '{}' not found. Setting package to null.", milestonePackageName);
                    return null;
                });

        Appointment appointment = new Appointment();
        appointment.setCustomerUser(customer);
        appointment.setVehicle(vehicle);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setEstimatedCost(recommendation.getEstimatedTotal());
        appointment.setServicePackage(servicePackageForMilestone);
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
        userRepository.findByIdAndRoleName(customerId, PredefinedRole.CUSTOMER)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByCustomerUserId(customerId);
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    public List<AppointmentResponse> getAppointmentByVehicleId(Long vehicleId) {
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByVehicleId(vehicleId);
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
            appointments = appointmentRepository.findByStatus(status);
        } else if (user.isStaff() || user.isTechnician()) {
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            appointments = appointmentRepository.findByStatusAndServiceCenterId(status, centerId);
        } else {
            return Collections.emptyList();
        }
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    public List<AppointmentResponse> getAppointmentByTechnicianId(Long technicianId) {
        userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByTechnicianUserId(technicianId);
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    @Transactional
    public AppointmentResponse assignTechnician(Long appointmentId, Long technicianId, Authentication authentication) {
        User staff = getAuthenticatedUser(authentication);
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        User technician = userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
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
        if (appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
        }
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapSingleAppointmentToResponse(savedAppointment);
    }

    @Override
    public List<AppointmentResponse> getAll(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        List<Appointment> appointments;
        if (user.isAdmin()) {
            appointments = appointmentRepository.findAll();
        } else if (user.isStaff() || user.isTechnician()) {
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            appointments = appointmentRepository.findAllByServiceCenterId(centerId);
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
            appointments = appointmentRepository.findByAppointmentDateBetween(startDate, endDate);
        } else if (user.isStaff() || user.isTechnician()) {
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            appointments = appointmentRepository.findByAppointmentDateBetweenAndServiceCenterId(startDate, endDate, centerId);
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
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new AppException(ErrorCode.STATUS_INVALID);
        }

        if (requests == null || requests.isEmpty()) {
            log.warn("upgradeServiceItem called with no requests for appointment {}", appointmentId);
            return mapSingleAppointmentToResponse(appointment);
        }

        // Lấy danh sách giá nâng cấp (REPLACE) một lần
        List<ModelPackageItem> replacePriceDefinitions = modelPackageItemRepository
                .findAllByVehicleModelId(appointment.getVehicle().getModel().getId())
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

        // 1. Bắt đầu vòng lặp
        for (ServiceItemApproveRequest request : requests) {

            AppointmentServiceItemDetail detail = appointmentServiceItemDetailRepository.findById(request.getAppointmentServiceDetailId())
                    .orElseThrow(() -> new AppException(ErrorCode.DETAIL_NOT_FOUND)); // (Sử dụng ErrorCode mới)

            if (!detail.getAppointment().getId().equals(appointmentId)) {
                log.error("Staff {} tried to approve detail {} which does not belong to appointment {}", staff.getUsername(), detail.getId(), appointmentId);
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            if (detail.getCustomerApproved().equals(request.getApproved())) {
                continue; // Bỏ qua nếu trạng thái đã giống
            }

            if (request.getApproved()) {
                // Khách hàng ĐỒNG Ý nâng cấp
                detail.setCustomerApproved(true);
            } else {
                // Khách hàng TỪ CHỐI (Quay về CHECK)
                log.warn("Staff reverting item {} for appointment {} back to CHECK", detail.getServiceItem().getId(), appointmentId);

                // Tìm lại giá CHECK gốc
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
            // (Không save ở đây, để save 1 lần ở cuối)
        }

        // 2. Cập nhật lịch hẹn (sau khi vòng lặp kết thúc)
        appointment.recalculateEstimatedCost(); // Tính lại tổng tiền

        // Kiểm tra xem tất cả các hạng mục (sau khi cập nhật) đã được duyệt chưa
        boolean allApproved = appointment.getServiceDetails().stream()
                .allMatch(AppointmentServiceItemDetail::getCustomerApproved);

        if (allApproved && appointment.getStatus() == AppointmentStatus.WAITING_FOR_APPROVAL) {
            // Nếu tất cả đã được xử lý (duyệt hoặc từ chối), trả về CONFIRMED
            appointment.setStatus(AppointmentStatus.CONFIRMED);
        }

        Appointment savedAppointment = appointmentRepository.save(appointment); // Lưu 1 lần
        log.info("Staff {} set approval for {} item(s) for appointment {}", staff.getUsername(), requests.size(), appointmentId);

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
}