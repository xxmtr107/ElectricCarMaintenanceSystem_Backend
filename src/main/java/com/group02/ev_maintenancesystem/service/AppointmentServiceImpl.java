package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO; // Thêm import
import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerAppointmentRequest;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.entity.*;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
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

        // ------------------------------------------------------------------
        // 3. LẤY ĐỀ XUẤT BẢO DƯỠNG ĐẾN HẠN (SỬ DỤNG LOGIC MỚI)
        // ------------------------------------------------------------------
        List<MaintenanceRecommendationDTO> recommendations = maintenanceService.getRecommendations(vehicle.getId());

        if (recommendations.isEmpty()) {
            // Xử lý trường hợp không có đề xuất nào
            // Tùy chọn 1: Ném lỗi
            throw new AppException(ErrorCode.UNCATEGORIZED); // Hoặc ErrorCode.NO_MAINTENANCE_DUE
            // Tùy chọn 2: Tạo lịch hẹn kiểm tra chung (nếu có định nghĩa)
            // Tùy chọn 3: Cho phép tạo lịch hẹn không có hạng mục (cần xem lại logic sau này)
            /*
            log.warn("No maintenance recommendation found for Vehicle ID {}. Creating appointment without specific items.", vehicle.getId());
            Appointment appointment = new Appointment();
            appointment.setCustomerUser(customer);
            appointment.setVehicle(vehicle);
            appointment.setAppointmentDate(request.getAppointmentDate());
            appointment.setStatus(AppointmentStatus.PENDING);
            appointment.setEstimatedCost(BigDecimal.ZERO); // Không có chi phí ước tính
            appointment.setServicePackage(null); // Không có gói cụ thể
            appointment.setServiceItems(Collections.emptyList()); // Danh sách hạng mục rỗng
            // Gán Service Center nếu có
            Appointment savedAppointment = appointmentRepository.save(appointment);
            return appointmentMapper.toAppointmentResponse(savedAppointment);
            */
        }

        // Lấy đề xuất đầu tiên (được coi là ưu tiên nhất)
        MaintenanceRecommendationDTO recommendation = recommendations.get(0);
        Integer recommendedMilestoneKm = recommendation.getMilestoneKm();

        // ------------------------------------------------------------------
        // 4. TÌM ServicePackage TƯƠNG ỨNG VỚI CỘT MỐC (Logic Mới)
        // ------------------------------------------------------------------
        // Giả định tên ServicePackage là "Bảo dưỡng mốc Xkm"
        String milestonePackageName = "Bảo dưỡng mốc " + recommendedMilestoneKm + "km";
        ServicePackage servicePackageForMilestone = servicePackageRepository.findByName(milestonePackageName)
                .orElseGet(() -> {
                    // Nếu không tìm thấy, có thể tạo mới hoặc dùng gói mặc định/null
                    log.warn("ServicePackage with name '{}' not found. Consider adding it to data.sql. Setting package to null for appointment.", milestonePackageName);
                    return null; // Hoặc tìm/tạo một gói chung chung
                });

        // ------------------------------------------------------------------
        // 5. LẤY DANH SÁCH ServiceItem TỪ ĐỀ XUẤT (Logic Mới)
        // ------------------------------------------------------------------
        List<Long> recommendedServiceItemIds = recommendation.getItems().stream()
                .map(itemDto -> itemDto.getServiceItem() != null ? itemDto.getServiceItem().getId() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<ServiceItem> serviceItemsForAppointment;
        if (!recommendedServiceItemIds.isEmpty()) {
            serviceItemsForAppointment = serviceItemRepository.findAllById(recommendedServiceItemIds);
            // Kiểm tra xem có lấy đủ số lượng item không
            if (serviceItemsForAppointment.size() != recommendedServiceItemIds.size()) {
                log.warn("Mismatch between recommended service item IDs and found entities for milestone {}km.", recommendedMilestoneKm);
                // Có thể ném lỗi hoặc tiếp tục với những gì tìm được
            }
        } else {
            log.warn("Recommendation for milestone {}km has no service items listed.", recommendedMilestoneKm);
            serviceItemsForAppointment = Collections.emptyList();
        }


        // ------------------------------------------------------------------
        // 6. TẠO APPOINTMENT ENTITY (Logic Mới)
        // ------------------------------------------------------------------
        Appointment appointment = new Appointment();
        appointment.setCustomerUser(customer);
        appointment.setVehicle(vehicle);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setEstimatedCost(recommendation.getEstimatedTotal()); // Lấy tổng giá từ đề xuất
        appointment.setServicePackage(servicePackageForMilestone); // Gán gói/cấp độ mốc
        appointment.setServiceItems(serviceItemsForAppointment); // Gán danh sách hạng mục
        // Gán Service Center mặc định hoặc dựa trên logic khác nếu cần
        // appointment.setServiceCenter(...);

        // 7. Lưu Appointment và Map sang Response
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Mapper sẽ tự động xử lý việc lấy giá đúng cho từng item dựa trên Context
        AppointmentResponse response = appointmentMapper.toAppointmentResponse(savedAppointment);
        // Không cần gọi mapServiceItems thủ công nếu @AfterMapping đã được cấu hình đúng trong AppointmentMapper

        log.info("Successfully created appointment ID {} for vehicle ID {} at milestone {}km.", savedAppointment.getId(), vehicle.getId(), recommendedMilestoneKm);
        return response;
    }

    // --- CÁC PHƯƠNG THỨC KHÁC ---
    // (Xem lại và đảm bảo chúng vẫn hoạt động đúng, đặc biệt là phần mapping response)

    @Override
    public List<AppointmentResponse> getAppointmentByCustomerId(Long customerId) {
        // Kiểm tra customer tồn tại
        userRepository.findByIdAndRoleName(customerId, PredefinedRole.CUSTOMER)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByCustomerUserId(customerId);
        // Map và đảm bảo giá item đúng
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    public List<AppointmentResponse> getAppointmentByVehicleId(Long vehicleId) {
        // Kiểm tra vehicle tồn tại
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByVehicleId(vehicleId);
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    public AppointmentResponse getAppointmentByAppointmentId(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        // Map và đảm bảo giá item đúng
        return mapSingleAppointmentToResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> getAppointmentByStatus(AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByStatus(status);
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    public List<AppointmentResponse> getAppointmentByTechnicianId(Long technicianId) {
        // Kiểm tra technician tồn tại
        userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByTechnicianUserId(technicianId);
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    @Transactional
    public AppointmentResponse assignTechnician(Long appointmentId, Long technicianId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        User technician = userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Kiểm tra xem technician có sẵn không (logic này phức tạp, tạm bỏ qua)
        // boolean isAvailable = isTechnicianAvailable(technicianId, appointment.getAppointmentDate());
        // if (!isAvailable) {
        //     throw new AppException(ErrorCode.TECHNICIAN_NOT_AVAILABLE);
        // }

        appointment.setTechnicianUser(technician);
        // Chỉ đổi status thành CONFIRMED nếu đang là PENDING
        if (appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapSingleAppointmentToResponse(savedAppointment);
    }

    @Override
    public List<AppointmentResponse> getAll() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    public List<AppointmentResponse> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        List<Appointment> appointments = appointmentRepository.findByAppointmentDateBetween(startDate, endDate);
        return mapAppointmentListToResponse(appointments);
    }

    @Override
    @Transactional
    public AppointmentResponse setStatusAppointment(Long id, AppointmentStatus newStatus) {
        if (newStatus == null) {
            throw new AppException(ErrorCode.STATUS_INVALID);
        }
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // Logic bổ sung: không cho chuyển từ COMPLETED/CANCELLED sang trạng thái khác? (Tùy yêu cầu)
        if (appointment.getStatus() == AppointmentStatus.COMPLETED || appointment.getStatus() == AppointmentStatus.CANCELLED) {
            if (appointment.getStatus() != newStatus) { // Cho phép set lại chính status đó
                log.warn("Attempted to change status of already {} appointment ID {}", appointment.getStatus(), id);
                // throw new AppException(ErrorCode.STATUS_INVALID); // Hoặc chỉ log và không làm gì
                return mapSingleAppointmentToResponse(appointment); // Trả về trạng thái hiện tại
            }
        }
        // Logic bổ sung: Cần assign technician trước khi COMPLETED?
        if (newStatus == AppointmentStatus.COMPLETED && appointment.getTechnicianUser() == null) {
            throw new AppException(ErrorCode.TECHNICIAN_NOT_ASSIGNED); // Cần tạo ErrorCode này
        }


        appointment.setStatus(newStatus);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Tạo Maintenance Record nếu trạng thái là COMPLETED
        if (newStatus == AppointmentStatus.COMPLETED) {
            maintenanceRecordService.createMaintenanceRecord(savedAppointment);
        }

        return mapSingleAppointmentToResponse(savedAppointment);
    }

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentUpdateRequest appointment) {
        // Phương thức này có thể cần được định nghĩa lại hoặc bỏ đi
        // vì logic update chủ yếu là assignTechnician và setStatusAppointment
        throw new UnsupportedOperationException("Use assignTechnician or setStatusAppointment instead.");
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(Long appointmentId, Authentication authentication) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long currentUserId = jwt.getClaim("userId");
        String rolesClaim = jwt.getClaim("scope"); // Lấy chuỗi scope
        // Chuyển scope thành Set để dễ kiểm tra
        Set<String> roles = rolesClaim != null ? Set.of(rolesClaim.split(" ")) : Collections.emptySet();


        // Validate quyền hủy
        boolean isAdminOrStaff = roles.contains("ROLE_ADMIN") || roles.contains("ROLE_STAFF");
        boolean isOwnerCustomer = roles.contains("ROLE_CUSTOMER") && appointment.getCustomerUser().getId().equals(currentUserId);
        boolean isAssignedTechnician = roles.contains("ROLE_TECHNICIAN") && appointment.getTechnicianUser() != null && appointment.getTechnicianUser().getId().equals(currentUserId);

        if (!isAdminOrStaff && !isOwnerCustomer && !isAssignedTechnician) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Validate trạng thái có thể hủy
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppException(ErrorCode.CANNOT_CANCEL_COMPLETED_APPOINTMENT);
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            // Có thể trả về luôn thông tin hiện tại hoặc ném lỗi tùy logic
            // throw new AppException(ErrorCode.APPOINTMENT_ALREADY_CANCELLED);
            log.info("Appointment ID {} is already cancelled.", appointmentId);
            return mapSingleAppointmentToResponse(appointment);
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return mapSingleAppointmentToResponse(savedAppointment);
    }

    // --- HÀM HỖ TRỢ MAPPING ---
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
        // Gọi mapper chính
        AppointmentResponse response = appointmentMapper.toAppointmentResponse(appointment);
        // Đảm bảo logic @AfterMapping trong mapper được thực thi để tính giá item
        // Nếu mapper không dùng @Context, bạn có thể cần gọi hàm mapServiceItems thủ công ở đây
        // appointmentMapper.mapServiceItems(appointment, response, modelPackageItemRepository); // Bỏ comment nếu cần
        return response;
    }

    // Phương thức isTechnicianAvailable cần logic phức tạp hơn (kiểm tra lịch làm việc, các appointment khác...)
    // public boolean isTechnicianAvailable(long technicianId, LocalDateTime scheduleDate) { ... }
}