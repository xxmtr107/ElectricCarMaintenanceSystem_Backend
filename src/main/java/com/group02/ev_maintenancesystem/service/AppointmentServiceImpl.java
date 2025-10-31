package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO; // Thêm import
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO;
import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerAppointmentRequest;
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
    MaintenanceService maintenanceService; // Đã inject từ các bước trước
    ServiceCenterRepository serviceCenterRepository;


    @Override
    @Transactional
    public AppointmentResponse createAppointmentByCustomer(Authentication authentication, CustomerAppointmentRequest request) {
        // 1. Lấy thông tin Customer và Vehicle (Giữ nguyên logic cũ)
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


        // 2. Kiểm tra trùng lịch hẹn trong ngày (Giữ nguyên logic cũ)
        LocalDate appointmentDay = request.getAppointmentDate().toLocalDate();
        LocalDateTime startOfDay = appointmentDay.atStartOfDay();
        LocalDateTime endOfDay = appointmentDay.atTime(LocalTime.MAX);
        List<Appointment> existingAppointments = appointmentRepository
                .findByVehicleIdAndAppointmentDateBetween(request.getVehicleId(), startOfDay, endOfDay);
        boolean hasActiveAppointment = existingAppointments.stream()
                .anyMatch(app -> app.getStatus() != AppointmentStatus.CANCELLED && app.getStatus() != AppointmentStatus.COMPLETED); // Bổ sung COMPLETED
        if (hasActiveAppointment) {
            throw new AppException(ErrorCode.APPOINTMENT_ALREADY_EXISTS);
        }

        // 3. LẤY ĐỀ XUẤT BẢO DƯỠNG ĐẾN HẠN (Giữ nguyên logic cũ)
        List<MaintenanceRecommendationDTO> recommendations = maintenanceService.getRecommendations(vehicle.getId());

        if (recommendations.isEmpty()) {
            throw new AppException(ErrorCode.UNCATEGORIZED); // Hoặc ErrorCode.NO_MAINTENANCE_DUE
        }

        // Lấy đề xuất đầu tiên (được coi là ưu tiên nhất)
        MaintenanceRecommendationDTO recommendation = recommendations.get(0);
        Integer recommendedMilestoneKm = recommendation.getMilestoneKm();

        // 4. TÌM ServicePackage TƯƠNG ỨNG VỚI CỘT MỐC (Giữ nguyên logic cũ)
        String milestonePackageName = "Maintenance " + recommendedMilestoneKm + "km milestone";
        ServicePackage servicePackageForMilestone = servicePackageRepository.findByName(milestonePackageName)
                .orElseGet(() -> {
                    log.warn("ServicePackage with name '{}' not found. Consider adding it to data.sql. Setting package to null for appointment.", milestonePackageName);
                    return null;
                });

        // 5. LẤY DANH SÁCH ServiceItem TỪ ĐỀ XUẤT (Giữ nguyên logic cũ)
        List<Long> recommendedServiceItemIds = recommendation.getItems().stream()
                .map(itemDto -> itemDto.getServiceItem() != null ? itemDto.getServiceItem().getId() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<ServiceItem> serviceItemsForAppointment;
        if (!recommendedServiceItemIds.isEmpty()) {
            serviceItemsForAppointment = serviceItemRepository.findAllById(recommendedServiceItemIds);
            if (serviceItemsForAppointment.size() != recommendedServiceItemIds.size()) {
                log.warn("Mismatch between recommended service item IDs and found entities for milestone {}km.", recommendedMilestoneKm);
            }
        } else {
            log.warn("Recommendation for milestone {}km has no service items listed.", recommendedMilestoneKm);
            serviceItemsForAppointment = Collections.emptyList();
        }


        // 6. TẠO APPOINTMENT ENTITY (Logic Mới)
        Appointment appointment = new Appointment();
        appointment.setCustomerUser(customer);
        appointment.setVehicle(vehicle);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setEstimatedCost(recommendation.getEstimatedTotal()); // Lấy tổng giá từ đề xuất
        appointment.setServicePackage(servicePackageForMilestone); // Gán gói/cấp độ mốc
        appointment.setServiceItems(serviceItemsForAppointment); // Gán danh sách hạng mục
        appointment.setServiceCenter(serviceCenter);
        appointment.setMilestoneKm(recommendedMilestoneKm); // <-- *** ADD THIS LINE ***

        // 7. Lưu Appointment và Map sang Response
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // --- SỬA Ở ĐÂY ---
        // Thay vì gọi mapper trực tiếp, gọi hàm helper đã chỉnh sửa
        AppointmentResponse response = mapSingleAppointmentToResponse(savedAppointment);
        // --- KẾT THÚC SỬA ---

        log.info("Successfully created appointment ID {} for vehicle ID {} at milestone {}km.", savedAppointment.getId(), vehicle.getId(), recommendedMilestoneKm);
        return response;
    }

    // --- CÁC PHƯƠNG THỨC KHÁC ---

    @Override
    public List<AppointmentResponse> getAppointmentByCustomerId(Long customerId) {
        userRepository.findByIdAndRoleName(customerId, PredefinedRole.CUSTOMER)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByCustomerUserId(customerId);
        return mapAppointmentListToResponse(appointments); // Đã gọi helper
    }

    @Override
    public List<AppointmentResponse> getAppointmentByVehicleId(Long vehicleId) {
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByVehicleId(vehicleId);
        return mapAppointmentListToResponse(appointments); // Đã gọi helper
    }

    @Override
    public AppointmentResponse getAppointmentByAppointmentId(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        return mapSingleAppointmentToResponse(appointment); // Đã gọi helper
    }

    @Override
    public List<AppointmentResponse> getAppointmentByStatus(AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByStatus(status);
        return mapAppointmentListToResponse(appointments); // Đã gọi helper
    }

    @Override
    public List<AppointmentResponse> getAppointmentByTechnicianId(Long technicianId) {
        userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByTechnicianUserId(technicianId);
        return mapAppointmentListToResponse(appointments); // Đã gọi helper
    }

    @Override
    @Transactional
    public AppointmentResponse assignTechnician(Long appointmentId, Long technicianId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        User technician = userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        appointment.setTechnicianUser(technician);
        if (appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapSingleAppointmentToResponse(savedAppointment); // Đã gọi helper
    }

    @Override
    public List<AppointmentResponse> getAll() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return mapAppointmentListToResponse(appointments); // Đã gọi helper
    }

    @Override
    public List<AppointmentResponse> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        List<Appointment> appointments = appointmentRepository.findByAppointmentDateBetween(startDate, endDate);
        return mapAppointmentListToResponse(appointments); // Đã gọi helper
    }

    @Override
    @Transactional
    public AppointmentResponse setStatusAppointment(Long id, AppointmentStatus newStatus) {
        if (newStatus == null) {
            throw new AppException(ErrorCode.STATUS_INVALID);
        }
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

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

        return mapSingleAppointmentToResponse(savedAppointment); // Đã gọi helper
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

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long currentUserId = jwt.getClaim("userId");
        String rolesClaim = jwt.getClaim("scope");
        Set<String> roles = rolesClaim != null ? Set.of(rolesClaim.split(" ")) : Collections.emptySet();

        boolean isAdminOrStaff = roles.contains("ROLE_ADMIN") || roles.contains("ROLE_STAFF");
        boolean isOwnerCustomer = roles.contains("ROLE_CUSTOMER") && appointment.getCustomerUser().getId().equals(currentUserId);
        boolean isAssignedTechnician = roles.contains("ROLE_TECHNICIAN") && appointment.getTechnicianUser() != null && appointment.getTechnicianUser().getId().equals(currentUserId);

        if (!isAdminOrStaff && !isOwnerCustomer && !isAssignedTechnician) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppException(ErrorCode.CANNOT_CANCEL_COMPLETED_APPOINTMENT);
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            log.info("Appointment ID {} is already cancelled.", appointmentId);
            return mapSingleAppointmentToResponse(appointment); // Đã gọi helper
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return mapSingleAppointmentToResponse(savedAppointment); // Đã gọi helper
    }

    // --- HÀM HỖ TRỢ MAPPING ---
    private List<AppointmentResponse> mapAppointmentListToResponse(List<Appointment> appointments) {
        if (appointments == null || appointments.isEmpty()) {
            return Collections.emptyList();
        }
        return appointments.stream()
                .map(this::mapSingleAppointmentToResponse) // Sử dụng helper mới
                .collect(Collectors.toList());
    }

    private AppointmentResponse mapSingleAppointmentToResponse(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        // Bước 1: Gọi mapper để map các trường cơ bản
        AppointmentResponse response = appointmentMapper.toAppointmentResponse(appointment);

        // Bước 2: Kiểm tra và lấy thông tin cần thiết để tra giá
        if (appointment.getVehicle() == null || appointment.getVehicle().getModel() == null || appointment.getServiceItems() == null) {
            return response;
        }

        VehicleModel model = appointment.getVehicle().getModel();
        Integer milestoneKm = appointment.getMilestoneKm(); // <-- *** USE THE SAVED MILESTONE ***


        // SỬA LỖI: Xử lý dữ liệu cũ không có milestoneKm
        if (milestoneKm == null) {
            log.warn("milestoneKm is null for appointment ID {}. Attempting to infer from estimatedCost.", appointment.getId());

            // Lấy tất cả các mốc KM có thể có của model này
            List<Integer> possibleMilestones = modelPackageItemRepository
                    .findByVehicleModelId(model.getId()).stream()
                    .map(ModelPackageItem::getMilestoneKm)
                    .distinct()
                    .collect(Collectors.toList());

            for (Integer ms : possibleMilestones) {
                // Tính tổng giá của mốc KM đang xét
                BigDecimal total = modelPackageItemRepository
                        .findByVehicleModelIdAndMilestoneKm(model.getId(), ms).stream()
                        .map(ModelPackageItem::getPrice)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // So sánh với estimatedCost (dùng compareTo cho BigDecimal)
                if (appointment.getEstimatedCost() != null && total.compareTo(appointment.getEstimatedCost()) == 0) {
                    milestoneKm = ms; // Đã tìm thấy mốc KM
                    response.setMilestoneKm(ms); // Cập nhật luôn vào response
                    log.info("Inferred milestone {}km for appointment ID {}", ms, appointment.getId());
                    break;
                }
            }
        }
        // Kết thúc sửa lỗi


        if (milestoneKm == null) {
            log.warn("Could not determine milestoneKm for appointment ID {}, cannot determine item prices.", appointment.getId());
            return response;
        }

        // *** BƯỚC 4: LOGIC ĐÚNG THEO YÊU CẦU CỦA BẠN ***
        List<ModelPackageItemDTO> modelPackageItemDTOs = new ArrayList<>();
        List<ServiceItem> serviceItems = appointment.getServiceItems(); // Lấy list entity

        for (ServiceItem item : serviceItems) {

            // Tìm giá và actionType từ ModelPackageItem
            Optional<ModelPackageItem> mpiOpt = modelPackageItemRepository
                    .findByVehicleModelIdAndMilestoneKmAndServiceItemId(model.getId(), milestoneKm, item.getId());

            BigDecimal price = mpiOpt.map(ModelPackageItem::getPrice).orElse(BigDecimal.ZERO);
            MaintenanceActionType actionType = mpiOpt.map(ModelPackageItem::getActionType).orElse(null);

            // 1. Tạo DTO lồng bên trong (ServiceItemDTO)
            ServiceItemDTO nestedItemDTO = ServiceItemDTO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .build();

            // 2. Tạo DTO bên ngoài (ModelPackageItemDTO)
            ModelPackageItemDTO outerItemDTO = new ModelPackageItemDTO(
                    nestedItemDTO,
                    price,
                    actionType
            );

            modelPackageItemDTOs.add(outerItemDTO); // Thêm DTO bên ngoài vào danh sách
        }

        response.setServiceItems(modelPackageItemDTOs); // Set danh sách DTO đã build
        // *** KẾT THÚC THAY ĐỔI ***

        return response;
    }

}