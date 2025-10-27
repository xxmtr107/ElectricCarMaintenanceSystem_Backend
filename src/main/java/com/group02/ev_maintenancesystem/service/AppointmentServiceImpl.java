package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
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
    MaintenanceService maintenanceService;

    @Override
    @Transactional // Quan trọng: Đảm bảo transaction cho cả quá trình
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

        // 2. Kiểm tra trùng lịch hẹn trong ngày
        LocalDate appointmentDay = request.getAppointmentDate().toLocalDate();
        LocalDateTime startOfDay = appointmentDay.atStartOfDay();
        LocalDateTime endOfDay = appointmentDay.atTime(LocalTime.MAX);
        List<Appointment> existingAppointments = appointmentRepository
                .findByVehicleIdAndAppointmentDateBetween(request.getVehicleId(), startOfDay, endOfDay);
        boolean hasActiveAppointment = existingAppointments.stream()
                .anyMatch(app -> app.getStatus() != AppointmentStatus.CANCELLED);
        if (hasActiveAppointment) {
            throw new AppException(ErrorCode.APPOINTMENT_ALREADY_EXISTS);
        }

        // ------------------------------------------------------------------
        // 3. LẤY ĐỀ XUẤT BẢO DƯỠNG ĐẾN HẠN
        // ------------------------------------------------------------------
        List<MaintenanceRecommendationDTO> recommendations = maintenanceService.getRecommendations(vehicle.getId());

        if (recommendations.isEmpty()) {
            // Có thể throw lỗi nếu không có gì để bảo dưỡng, hoặc cho phép tạo lịch hẹn "rỗng"
            // Hoặc tạo lịch hẹn kiểm tra tổng quát (nếu có định nghĩa gói/item cho việc này)
            throw new AppException(ErrorCode.UNCATEGORIZED); // Hoặc một ErrorCode phù hợp hơn, ví dụ: NO_MAINTENANCE_DUE
        }

        // Lấy đề xuất ưu tiên nhất (theo logic trong getRecommendations)
        MaintenanceRecommendationDTO recommendedMilestone = recommendations.get(0);
        Long recommendedPackageId = recommendedMilestone.getPackageId(); // ID của cấp độ bảo dưỡng đến hạn

        // 4. Lấy thông tin ServicePackage (Cấp độ bảo dưỡng) tương ứng
        ServicePackage servicePackage = servicePackageRepository.findById(recommendedPackageId)
                .orElseThrow(() -> {
                    log.error("Recommended ServicePackage with ID {} not found, though recommendation was generated.", recommendedPackageId);
                    return new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND); // Lỗi logic nội bộ nếu xảy ra
                });


        // ------------------------------------------------------------------
        // 5. LẤY DANH SÁCH HẠNG MỤC VÀ TÍNH GIÁ (Logic Mới)
        // ------------------------------------------------------------------
        Long modelId = vehicle.getModel().getId();
        List<ModelPackageItem> modelItems = modelPackageItemRepository
                .findByVehicleModelIdAndServicePackageId(modelId, recommendedPackageId);

        if (modelItems.isEmpty()) {
            log.warn("No ModelPackageItem found for VehicleModel ID {} and recommended ServicePackage ID {}. Creating appointment without items.", modelId, recommendedPackageId);
            // Quyết định: throw lỗi hay tạo appointment không có item? Tạm thời tạo không item
        }

        // Lấy danh sách ServiceItem entities từ ModelPackageItem
        List<ServiceItem> serviceItemsForAppointment = modelItems.stream()
                .map(ModelPackageItem::getServiceItem)
                .filter(Objects::nonNull) // Bỏ qua nếu ServiceItem bị null
                .collect(Collectors.toList());

        // Tính tổng chi phí ước tính dựa trên giá trong modelItems
        BigDecimal totalCost = modelItems.stream()
                .map(ModelPackageItem::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ------------------------------------------------------------------
        // 6. TẠO APPOINTMENT ENTITY (Logic Mới)
        // ------------------------------------------------------------------
        Appointment appointment = new Appointment();
        appointment.setCustomerUser(customer);
        appointment.setVehicle(vehicle);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.PENDING); // Trạng thái ban đầu
        appointment.setEstimatedCost(totalCost);
        appointment.setServicePackage(servicePackage); // Gán gói/cấp độ được đề xuất
        appointment.setServiceItems(serviceItemsForAppointment); // Gán danh sách hạng mục tương ứng

        // Có thể gán ServiceCenter mặc định hoặc dựa trên logic khác nếu cần
        // appointment.setServiceCenter(...);

        // 7. Lưu Appointment và Map sang Response (Giữ nguyên logic cũ, nhưng mapper cần hoạt động đúng)
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Dùng Mapper để chuyển đổi sang Response DTO
        AppointmentResponse response = appointmentMapper.toAppointmentResponse(savedAppointment);
        // Đảm bảo mapper xử lý đúng việc lấy giá từ modelPackageItemRepository khi map serviceItems sang DTO
        // Nếu AppointmentMapper đã được cập nhật ở Bước 2 thì không cần sửa gì thêm ở đây
        // appointmentMapper.mapServiceItems(savedAppointment, response, modelPackageItemRepository); // Gọi lại nếu cần thiết, hoặc đảm bảo @AfterMapping chạy đúng

        return response;
    }
//    Customer role
//    @Override
//    @Transactional
//    public AppointmentResponse createAppointmentByCustomer(Authentication authentication, CustomerAppointmentRequest request) {
//        Appointment appointment = appointmentMapper.toAppointmentCustomer(request);
//
//        Jwt jwt = (Jwt) authentication.getPrincipal();
//        Long customerId = jwt.getClaim("userId");
//
//        // Validate: Khách hàng tồn tại
//        User customer = userRepository.findByIdAndRoleName(customerId, PredefinedRole.CUSTOMER)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//
//        // Validate: Xe tồn tại
//        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
//                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
//
//        // Validate: Xe phải thuộc về khách hàng đang đăng nhập
//        if(!vehicle.getCustomerUser().getId().equals(customerId)) {
//            throw new AppException(ErrorCode.VEHICLE_NOT_BELONG_TO_CUSTOMER);
//        }
//
//        // Validate: Xe không thể đặt trùng ngày giờ
//        LocalDate appointmentDay = request.getAppointmentDate().toLocalDate();
//
//        // Tính đầu ngày và cuối ngày (00:00:00 → 23:59:59)
//        LocalDateTime startOfDay = appointmentDay.atStartOfDay();
//        LocalDateTime endOfDay = appointmentDay.atTime(LocalTime.MAX);
//
//        // Tìm appointment trùng ngày (nếu có)
//        List<Appointment> existingAppointments = appointmentRepository
//                .findByVehicleIdAndAppointmentDateBetween(
//                        request.getVehicleId(),
//                        startOfDay,
//                        endOfDay
//                );
//
//        // Chỉ throw exception nếu có appointment cũ KHÔNG phải CANCELLED
//        boolean hasActiveAppointment = existingAppointments.stream()
//                .anyMatch(app -> app.getStatus() != AppointmentStatus.CANCELLED);
//
//        if (hasActiveAppointment) {
//            throw new AppException(ErrorCode.APPOINTMENT_ALREADY_EXISTS);
//        }
//
//
//        // Validate: Phải chọn ít nhất 1 (gói hoặc dịch vụ lẻ)
//        if(request.getServicePackageId() == null &&
//                (request.getServiceItemIds() == null || request.getServiceItemIds().isEmpty())) {
//            throw new AppException(ErrorCode.MUST_SELECT_SERVICE);
//        }
//
//        appointment.setCustomerUser(customer);
//        appointment.setVehicle(vehicle);
//        appointment.setStatus(AppointmentStatus.PENDING);
//
//        BigDecimal totalCost = BigDecimal.ZERO;
//        Set<Long> packageServiceItemIds;
//        Long modelId = vehicle.getModel().getId();
//
//        // Xử lý gói dịch vụ nếu được chọn
//        if(request.getServicePackageId() != null) {
//            ServicePackage servicePackage = servicePackageRepository.findById(request.getServicePackageId())
//                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));
//
//            // Lấy các dịch vụ cụ thể cho model này và gói dịch vụ này
//            List<ModelPackageItem> modelItems = modelPackageItemRepository
//                    .findByVehicleModelIdAndServicePackageId(modelId, request.getServicePackageId());
//
////            if (modelItems.isEmpty()) {
////                throw new AppException(ErrorCode.SERVICE_PACKAGE_NOT_AVAILABLE_FOR_MODEL);
////            }
//
//            // Trích xuất service items từ modelItems
//            List<ServiceItem> packageItems = modelItems.stream()
//                    .map(ModelPackageItem::getServiceItem)
//                    .collect(Collectors.toList());
//
//            // Lưu ID của các dịch vụ trong gói để kiểm tra trùng lặp sau này
//            packageServiceItemIds = packageItems.stream()
//                    .map(ServiceItem::getId)
//                    .collect(Collectors.toSet());
//
//            // Tính tổng chi phí gói dịch vụ dựa trên model xe
//            BigDecimal packageCost = modelItems.stream()
//                    .map(ModelPackageItem::getPrice)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            appointment.setServicePackage(servicePackage);
//
//            // Thêm các service items vào appointment
//            if (appointment.getServiceItems() == null) {
//                appointment.setServiceItems(new ArrayList<>(packageItems));
//            } else {
//                appointment.getServiceItems().addAll(packageItems);
//            }
//
//            totalCost = totalCost.add(packageCost);
//        } else {
//            packageServiceItemIds = new HashSet<>();
//        }
//
//        // Xử lý dịch vụ lẻ nếu được chọn
//        if(request.getServiceItemIds() != null && !request.getServiceItemIds().isEmpty()) {
//            // Validate: Dịch vụ tồn tại
//            List<ServiceItem> serviceItems = serviceItemRepository.findAllByIdIn(request.getServiceItemIds());
//            if(serviceItems.size() != request.getServiceItemIds().size()) {
//                throw new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND);
//            }
//
//            // Validate: Không trùng lặp với dịch vụ trong gói
//            if (!packageServiceItemIds.isEmpty()) {
//                // Kiểm tra xem có dịch vụ lẻ nào đã có trong gói không
//                List<ServiceItem> duplicateItems = serviceItems.stream()
//                        .filter(item -> packageServiceItemIds.contains(item.getId()))
//                        .collect(Collectors.toList());
//
//                if (!duplicateItems.isEmpty()) {
//                    throw new AppException(ErrorCode.DUPLICATE_SERVICE_ITEMS);
//                }
//            }
//
//            // Tính chi phí dịch vụ lẻ theo model xe
//            BigDecimal serviceItemsTotal = BigDecimal.ZERO;
//            for (ServiceItem item : serviceItems) {
//                // Tìm giá theo model (dịch vụ lẻ có service_package_id là NULL)
//                Optional<ModelPackageItem> modelItem = modelPackageItemRepository
//                        .findByVehicleModelIdAndServicePackageIsNullAndServiceItemId(modelId, item.getId());
//
//                BigDecimal itemPrice;
//
//                itemPrice = modelItem.get().getPrice();
//
//
//                serviceItemsTotal = serviceItemsTotal.add(itemPrice);
//            }
//
//            // Thêm dịch vụ lẻ vào appointment
//            if (appointment.getServiceItems() == null) {
//                appointment.setServiceItems(new ArrayList<>(serviceItems));
//            } else {
//                appointment.getServiceItems().addAll(serviceItems);
//            }
//
//            totalCost = totalCost.add(serviceItemsTotal);
//        }
//
//        appointment.setEstimatedCost(totalCost);
//
//        Appointment savedAppointment = appointmentRepository.save(appointment);
//
//        // Truyền repository làm context
//        AppointmentResponse response = appointmentMapper.toAppointmentResponse(savedAppointment);
//        appointmentMapper.mapServiceItems(savedAppointment, response, modelPackageItemRepository);
//
//        return response;
//    }


    // Admin role
    @Override
    public List<AppointmentResponse> getAppointmentByCustomerId(Long customerId) {
        userRepository.findByIdAndRoleName(customerId, PredefinedRole.CUSTOMER)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByCustomerUserId(customerId);
        return appointments.stream()
                .map(appointment -> {
                    AppointmentResponse response = appointmentMapper.toAppointmentResponse(appointment);
                    appointmentMapper.mapServiceItems(appointment, response, modelPackageItemRepository);
                    return response;
                })
                .toList();
    }

    // Admin role
    @Override
    public List<AppointmentResponse> getAppointmentByVehicleId(Long vehicleId) {
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        List<Appointment> appointments = appointmentRepository.findByVehicleId(vehicleId);

        return appointments.stream()
                .map(appointment -> {
                    AppointmentResponse response = appointmentMapper.toAppointmentResponse(appointment);
                    appointmentMapper.mapServiceItems(appointment, response, modelPackageItemRepository);
                    return response;
                })
                .toList();
    }

    // Admin role
    @Override
    public AppointmentResponse getAppointmentByAppointmentId(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentResponse response = appointmentMapper.toAppointmentResponse(appointment);
        appointmentMapper.mapServiceItems(appointment, response, modelPackageItemRepository);
        return response;
    }

    // Admin role
    @Override
    public List<AppointmentResponse> getAppointmentByStatus(AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByStatus(status);
        return appointments.stream()
                .map(appointment -> {
                    AppointmentResponse response = appointmentMapper.toAppointmentResponse(appointment);
                    appointmentMapper.mapServiceItems(appointment, response, modelPackageItemRepository);
                    return response;
                })
                .toList();
    }

    // Admin role
    @Override
    public List<AppointmentResponse> getAppointmentByTechnicianId(Long technicianId) {
        userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Appointment> appointments = appointmentRepository.findByTechnicianUserId(technicianId);

        return appointments.stream()
                .map(appointment -> {
                    AppointmentResponse response = appointmentMapper.toAppointmentResponse(appointment);
                    appointmentMapper.mapServiceItems(appointment, response, modelPackageItemRepository);
                    return response;
                })
                .toList();
    }

    // Admin role
    @Override
    public AppointmentResponse assignTechnician(Long appointmentId, Long technicianId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        User technician = userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        appointment.setTechnicianUser(technician);
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    // Admin role
    @Override
    public List<AppointmentResponse> getAll() {
        return appointmentRepository.findAll().stream()
                .map(appointment -> {
                    AppointmentResponse response = appointmentMapper.toAppointmentResponse(appointment);
                    appointmentMapper.mapServiceItems(appointment, response, modelPackageItemRepository);
                    return response;
                })
                .toList();

    }

    // Admin role
    @Override
    public List<AppointmentResponse> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        List<Appointment> appointments = appointmentRepository.findByAppointmentDateBetween(startDate, endDate);

        return appointments.stream()
                .map(appointment -> {
                    AppointmentResponse response = appointmentMapper.toAppointmentResponse(appointment);
                    appointmentMapper.mapServiceItems(appointment, response, modelPackageItemRepository);
                    return response;
                })
                .toList();
    }
    @Override
    public AppointmentResponse setStatusAppointment(Long id, AppointmentStatus newStatus) {
        if(newStatus==null) {
            throw new AppException(ErrorCode.STATUS_INVALID);
        }
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // Update status
        appointment.setStatus(newStatus);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        if(newStatus == AppointmentStatus.COMPLETED) {
            maintenanceRecordService.createMaintenanceRecord(savedAppointment);
        }

        AppointmentResponse response = appointmentMapper.toAppointmentResponse(savedAppointment);
        appointmentMapper.mapServiceItems(savedAppointment, response, modelPackageItemRepository);
        return response;
    }

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentUpdateRequest appointment) {
        return null;
    }

    @Override
    public AppointmentResponse cancelAppointment(Long appointmentId, Authentication authentication) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long currentUserId = jwt.getClaim("userId");
        String role = jwt.getClaim("scope");

        // Validate ownership
        if (!role.contains("ADMIN") && !role.contains("STAFF")) {
            // Customer chỉ hủy được appointment của mình
            if (role.contains("CUSTOMER")) {
                if (!appointment.getCustomerUser().getId().equals(currentUserId)) {
                    throw new AppException(ErrorCode.UNAUTHORIZED);
                }
            }
            // Technician chỉ hủy được appointment được assign cho mình
            else if (role.contains("TECHNICIAN")) {
                if (appointment.getTechnicianUser() == null ||
                        !appointment.getTechnicianUser().getId().equals(currentUserId)) {
                    throw new AppException(ErrorCode.UNAUTHORIZED);
                }
            }
        }
        // Validate: Không thể hủy appointment đã hoàn thành
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppException(ErrorCode.CANNOT_CANCEL_COMPLETED_APPOINTMENT);
        }

        // Validate: Không thể hủy appointment đã bị hủy
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new AppException(ErrorCode.APPOINTMENT_ALREADY_CANCELLED);
        }


        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        AppointmentResponse response = appointmentMapper.toAppointmentResponse(savedAppointment);
        appointmentMapper.mapServiceItems(savedAppointment, response, modelPackageItemRepository);
        return response;
    }

}
