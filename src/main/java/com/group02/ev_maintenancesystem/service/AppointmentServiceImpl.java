package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO; // Thêm import
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

    // ... (Toàn bộ các repository)
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

    // ... (Hàm createAppointmentByCustomer, ... đã chuẩn)
    // ... (Hàm get..., assignTechnician..., getAll..., ... đã chuẩn)
    @Override
    @Transactional
    public AppointmentResponse createAppointmentByCustomer(Authentication authentication, CustomerAppointmentRequest request) {
        // --- THÊM VALIDATION GIỜ HÀNH CHÍNH (Yêu cầu 3) ---
        LocalTime appointmentTime = request.getAppointmentDate().toLocalTime();
        LocalTime startTime = LocalTime.of(7, 0); // 7:00 AM
        LocalTime endTime = LocalTime.of(18, 0); // 6:00 PM

        if (appointmentTime.isBefore(startTime) || appointmentTime.isAfter(endTime)) {
            throw new AppException(ErrorCode.APPOINTMENT_TIME_OUT_OF_BOUNDS);
        }
        // --- KẾT THÚC VALIDATION ---

        // 1. Lấy thông tin Customer và Vehicle
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

        // 2. Kiểm tra trùng lịch hẹn trong ngày
        LocalDate appointmentDay = request.getAppointmentDate().toLocalDate();
        LocalDateTime startOfDay = appointmentDay.atStartOfDay();
        LocalDateTime endOfDay = appointmentDay.atTime(LocalTime.MAX);
        List<Appointment> existingAppointments = appointmentRepository
                .findByVehicleIdAndAppointmentDateBetween(request.getVehicleId(), startOfDay, endOfDay);
        boolean hasActiveAppointment = existingAppointments.stream()
                .anyMatch(app -> app.getStatus() != AppointmentStatus.CANCELLED && app.getStatus() != AppointmentStatus.COMPLETED);
        if (hasActiveAppointment) {
            throw new AppException(ErrorCode.APPOINTMENT_ALREADY_EXISTS);
        }

        // 3. LẤY ĐỀ XUẤT BẢO DƯỠNG
        List<MaintenanceRecommendationDTO> recommendations = maintenanceService.getRecommendations(vehicle.getId());

        if (recommendations.isEmpty()) {
            // Hoặc ErrorCode.NO_MAINTENANCE_DUE (Cần tạo ErrorCode này)
            throw new AppException(ErrorCode.NO_MAINTENANCE_DUE);
        }

        MaintenanceRecommendationDTO recommendation = recommendations.get(0);
        Integer recommendedMilestoneKm = recommendation.getMilestoneKm();

        // 4. TÌM ServicePackage
        String milestonePackageName = "Maintenance " + recommendedMilestoneKm + "km milestone";
        ServicePackage servicePackageForMilestone = servicePackageRepository.findByName(milestonePackageName)
                .orElseGet(() -> {
                    log.warn("ServicePackage with name '{}' not found. Setting package to null.", milestonePackageName);
                    return null;
                });

        // 5. TẠO APPOINTMENT ENTITY
        Appointment appointment = new Appointment();
        appointment.setCustomerUser(customer);
        appointment.setVehicle(vehicle);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setEstimatedCost(recommendation.getEstimatedTotal());
        appointment.setServicePackage(servicePackageForMilestone);
        appointment.setServiceCenter(serviceCenter);
        appointment.setMilestoneKm(recommendedMilestoneKm);

        // 6. THAY ĐỔI LỚN (Yêu cầu 1): TẠO CÁC BẢN GHI CHI TIẾT DỊCH VỤ
        if (recommendation.getItems() == null || recommendation.getItems().isEmpty()) {
            log.warn("Recommendation for milestone {}km has no service items listed.", recommendedMilestoneKm);
            throw new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND);
        }

        for (ModelPackageItemDTO itemDto : recommendation.getItems()) {
            ServiceItem serviceItem = serviceItemRepository.findById(itemDto.getServiceItem().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND));

            // Tạo entity chi tiết
            AppointmentServiceItemDetail detail = AppointmentServiceItemDetail.builder()
                    .appointment(appointment) // Liên kết ngược
                    .serviceItem(serviceItem)
                    .actionType(itemDto.getActionType()) // Lấy action từ đề xuất (CHECK/REPLACE)
                    .price(itemDto.getPrice())         // Lấy giá từ đề xuất (GIÁ TRỌN GÓI)
                    .customerApproved(true)          // Gói gốc luôn được duyệt
                    .build();

            appointment.addServiceDetail(detail); // Thêm vào danh sách của appointment
        }

        // 7. Lưu Appointment (Cascade sẽ lưu luôn cả các chi tiết)
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
        // TODO: Thêm check bảo mật (user có quyền xem appointment này không)
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
            return Collections.emptyList(); // Customer không dùng API này
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

        // Yêu cầu 2: Bảo mật Center
        // 1. Staff phải cùng center với Appointment
        if (staff.isStaff() && (appointment.getServiceCenter() == null ||
                !appointment.getServiceCenter().getId().equals(staff.getServiceCenter().getId()))) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_BELONG_TO_SERVICE_CENTER);
        }
        // 2. Technician phải cùng center với Appointment
        if (appointment.getServiceCenter() != null && technician.getServiceCenter() != null) {
            if (!appointment.getServiceCenter().getId().equals(technician.getServiceCenter().getId())) {
                throw new AppException(ErrorCode.EMPLOYEE_NOT_BELONG_TO_SERVICE_CENTER);
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
            // Yêu cầu 2: Bảo mật Center
            Long centerId = user.getServiceCenter() != null ? user.getServiceCenter().getId() : -1L;
            appointments = appointmentRepository.findAllByServiceCenterId(centerId);
        } else {
            return Collections.emptyList(); // Customer dùng /my-appointments
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
            // Yêu cầu 2: Bảo mật Center
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

        // Yêu cầu 1 & 2: Bảo mật
        checkAppointmentAccess(user, appointment, "Only assigned technician or center staff can update status");

        // Nếu trạng thái là WAITING_FOR_APPROVAL, chỉ Staff/Admin mới được đổi
        if (appointment.getStatus() == AppointmentStatus.WAITING_FOR_APPROVAL &&
                newStatus != AppointmentStatus.CANCELLED) {
            if (user.isTechnician()) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            // Staff đang đổi status (ví dụ sang COMPLETED)
            if (newStatus == AppointmentStatus.COMPLETED) {
                // Check xem còn item nào chưa approve không
                boolean allApproved = appointment.getServiceDetails().stream()
                        .allMatch(AppointmentServiceItemDetail::getCustomerApproved);
                if (!allApproved) {
                    // Lỗi: Staff không thể Complete khi chưa duyệt hết
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

        // Tự động tạo MaintenanceRecord khi COMPLETED
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


    /**
     * HÀM MỚI (Yêu cầu 1): Nâng cấp Dịch vụ
     * Kịch bản: Tech đang CHECK (item 21, 35k), muốn đổi sang REPLACE (item 21, giá ???)
     * Vấn đề: data.sql không có giá REPLACE cho (VF5, 12k, item 21)
     * => Logic này không thể "nâng cấp" item 21.
     * => Logic đúng: Tech phải dùng PartUsageRequest để thêm vật tư (sơn, trám)
     * * HÀM NÀY CHỈ DÙNG KHI CÓ ĐỊNH NGHĨA GIÁ SONG SONG:
     * (3, 12000, 2, 45000, 'CHECK')
     * (3, 12000, 2, 600000, 'REPLACE') -- < Đây là mốc 24k
     * * Kịch bản đúng cho hàm này:
     * Xe mốc 12k km, Tech CHECK dầu phanh (item 2, 45k). Thấy bẩn.
     * Tech muốn "Nâng cấp" (upgrade) lên "Thay dầu phanh" (item 2, REPLACE).
     * Hệ thống phải tìm giá "Thay dầu phanh" (item 2, REPLACE) ở mốc *gần nhất*.
     * Đó là mốc 24k km, giá 600.000.
     */
    @Override
    @Transactional
    public AppointmentResponse upgradeServiceItem(Long appointmentId, ServiceItemUpgradeRequest request, Authentication authentication) {
        User technician = getAuthenticatedUser(authentication);
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // 1. Chỉ Technician được assign mới có quyền
        if (!technician.isTechnician() || appointment.getTechnicianUser() == null || !appointment.getTechnicianUser().getId().equals(technician.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // 2. Chỉ cho phép khi status là CONFIRMED (đang làm)
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new AppException(ErrorCode.STATUS_INVALID);
        }

        // 3. Tìm chi tiết dịch vụ
        AppointmentServiceItemDetail detail = appointmentServiceItemDetailRepository.findByAppointmentIdAndServiceItemId(appointmentId, request.getServiceItemId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_IN_APPOINTMENT));

        // 4. Nếu đã là REPLACE, không làm gì
        if (detail.getActionType() == request.getNewActionType()) {
            detail.setTechnicianNotes(request.getNotes());
            appointmentServiceItemDetailRepository.save(detail);
            return mapSingleAppointmentToResponse(appointment);
        }

        // 5. Chỉ cho phép nâng cấp từ CHECK -> REPLACE
        if (detail.getActionType() != MaintenanceActionType.CHECK || request.getNewActionType() != MaintenanceActionType.REPLACE) {
            throw new AppException(ErrorCode.ONLY_UPGRADE_CHECK_TO_REPLACE_ALLOWED);
        }

        // 6. Tìm giá mới (giá của REPLACE)
        // Tìm trong *tất cả* các mốc của model này
        Optional<ModelPackageItem> replacePriceOpt = modelPackageItemRepository
                .findByVehicleModelId(appointment.getVehicle().getModel().getId())
                .stream()
                // Lọc đúng service item
                .filter(item -> item.getServiceItem().getId().equals(request.getServiceItemId()))
                // Lọc đúng action type
                .filter(item -> item.getActionType() == MaintenanceActionType.REPLACE)
                // Lấy mốc đầu tiên tìm thấy (hoặc mốc gần nhất)
                .min(Comparator.comparing(ModelPackageItem::getMilestoneKm)); // Ưu tiên giá ở mốc thấp nhất

        if (replacePriceOpt.isEmpty()) {
            log.error("No REPLACE price defined for item {} for model {} in ANY milestone",
                    request.getServiceItemId(), appointment.getVehicle().getModel().getId());
            throw new AppException(ErrorCode.SERVICE_ITEM_PRICE_NOT_FOUND);
        }

        BigDecimal newPrice = replacePriceOpt.get().getPrice();

        // 7. Cập nhật chi tiết
        detail.setActionType(request.getNewActionType());
        detail.setPrice(newPrice); // Cập nhật giá trọn gói mới
        detail.setCustomerApproved(false); // CHỜ DUYỆT
        detail.setTechnicianNotes(request.getNotes());

        // 8. Cập nhật Appointment
        appointment.setStatus(AppointmentStatus.WAITING_FOR_APPROVAL);
        appointment.recalculateEstimatedCost(); // Tính lại tổng tiền (chỉ tính các mục approved)

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Technician {} upgraded item {} for appointment {}", technician.getUsername(), request.getServiceItemId(), appointmentId);

        return mapSingleAppointmentToResponse(savedAppointment);
    }

    /**
     * HÀM MỚI (Yêu cầu 1): Duyệt Dịch vụ
     */
    @Override
    @Transactional
    public AppointmentResponse approveServiceItem(Long appointmentId, ServiceItemApproveRequest request, Authentication authentication) {
        User staff = getAuthenticatedUser(authentication);
        // Chỉ Staff/Admin mới được duyệt
        if (!staff.isAdmin() && !staff.isStaff()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // Bảo mật Center (Yêu cầu 2)
        checkAppointmentAccess(staff, appointment, "Staff not in this service center");

        AppointmentServiceItemDetail detail = appointmentServiceItemDetailRepository.findById(request.getAppointmentServiceDetailId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_IN_APPOINTMENT));

        // Check xem detail này có thuộc appointment này không
        if (!detail.getAppointment().getId().equals(appointmentId)) {
            throw new AppException(ErrorCode.DETAIL_NOT_FOUND);
        }

        if (detail.getCustomerApproved().equals(request.getApproved())) {
            return mapSingleAppointmentToResponse(appointment); // Đã ở trạng thái mong muốn
        }

        if (request.getApproved()) {
            // DUYỆT
            detail.setCustomerApproved(true);
        } else {
            // TỪ CHỐI (Quay về CHECK)
            log.warn("Staff reverting item {} for appointment {} back to CHECK", detail.getServiceItem().getId(), appointmentId);

            // Tìm giá CHECK gốc (từ mốc hiện tại)
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
            detail.setCustomerApproved(true); // Quay về trạng thái đã duyệt (gói gốc)
        }

        // Cập nhật lại tổng tiền của Appointment (recalculate chỉ tính mục approved=true)
        appointment.recalculateEstimatedCost();

        // Kiểm tra xem tất cả đã được duyệt chưa
        boolean allApproved = appointment.getServiceDetails().stream()
                .allMatch(AppointmentServiceItemDetail::getCustomerApproved);

        if (allApproved && appointment.getStatus() == AppointmentStatus.WAITING_FOR_APPROVAL) {
            appointment.setStatus(AppointmentStatus.CONFIRMED); // Quay lại CONFIRMED để Tech làm tiếp
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Staff {} set approval={} for item detail {}", staff.getUsername(), request.getApproved(), request.getAppointmentServiceDetailId());

        return mapSingleAppointmentToResponse(savedAppointment);
    }


    // --- HÀM HỖ TRỢ MAPPING (SỬA ĐỔI) ---
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

        // 1. Map cơ bản
        AppointmentResponse response = appointmentMapper.toAppointmentResponse(appointment);

        // 2. Kiểm tra
        if (appointment.getVehicle() == null || appointment.getVehicle().getModel() == null || appointment.getServiceDetails() == null) {
            response.setServiceItems(Collections.emptyList()); // Đảm bảo không null
            return response;
        }

        // 3. THAY ĐỔI LỚN (Yêu cầu 1): Lấy chi tiết từ `serviceDetails`
        List<ModelPackageItemDTO> modelPackageItemDTOs = new ArrayList<>();

        for (AppointmentServiceItemDetail detail : appointment.getServiceDetails()) {
            ServiceItem item = detail.getServiceItem();
            if (item == null) continue;

            // 1. Tạo DTO lồng bên trong (ServiceItemDTO)
            ServiceItemDTO nestedItemDTO = ServiceItemDTO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .build();

            // 2. Tạo DTO bên ngoài (ModelPackageItemDTO)
            // Lấy giá và action từ bảng detail
            ModelPackageItemDTO outerItemDTO = new ModelPackageItemDTO(
                    nestedItemDTO,
                    detail.getPrice(),
                    detail.getActionType()
            );
            // TODO: Bổ sung thêm trường "approved", "notes" vào ModelPackageItemDTO nếu FE cần

            modelPackageItemDTOs.add(outerItemDTO);
        }

        response.setServiceItems(modelPackageItemDTOs);

        return response;
    }

    // Helper kiểm tra quyền truy cập Appointment cho Staff/Technician
    private void checkAppointmentAccess(User user, Appointment appointment, String errorMessage) {
        if (user.isAdmin()) {
            return; // Admin được
        }
        if (user.isStaff() && user.getServiceCenter() != null &&
                appointment.getServiceCenter() != null &&
                user.getServiceCenter().getId().equals(appointment.getServiceCenter().getId())) {
            return; // Staff đúng center
        }
        if (user.isTechnician() && appointment.getTechnicianUser() != null &&
                appointment.getTechnicianUser().getId().equals(user.getId())) {
            return; // Technician được assign
        }

        throw new AppException(ErrorCode.UNAUTHORIZED);
    }
}