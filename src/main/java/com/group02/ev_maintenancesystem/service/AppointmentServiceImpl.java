package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
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

    // Customer role
    @Override
    public AppointmentResponse createAppointmentByCustomer(Authentication authentication, CustomerAppointmentRequest request) {
        Appointment appointment = appointmentMapper.toAppointmentCustomer(request);

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long customerId = jwt.getClaim("userId");

        // Validate: Khách hàng tồn tại
        User customer = userRepository.findByIdAndRoleName(customerId, PredefinedRole.CUSTOMER)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Validate: Xe tồn tại
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        // Validate: Xe phải thuộc về khách hàng đang đăng nhập
        if(!vehicle.getCustomerUser().getId().equals(customerId)) {
            throw new AppException(ErrorCode.VEHICLE_NOT_BELONG_TO_CUSTOMER);
        }

        // Validate: Xe không thể đặt trùng ngày giờ
        LocalDate appointmentDay = request.getAppointmentDate().toLocalDate();

        // Tính đầu ngày và cuối ngày (00:00:00 → 23:59:59)
        LocalDateTime startOfDay = appointmentDay.atStartOfDay();
        LocalDateTime endOfDay = appointmentDay.atTime(LocalTime.MAX);

        boolean exists = appointmentRepository.existsByVehicleIdAndAppointmentDateBetween(
                request.getVehicleId(),
                startOfDay,
                endOfDay
        );

        if (exists) {
            throw new AppException(ErrorCode.APPOINTMENT_ALREADY_EXISTS);
        }


        // Validate: Phải chọn ít nhất 1 (gói hoặc dịch vụ lẻ)
        if(request.getServicePackageId() == null &&
                (request.getServiceItemIds() == null || request.getServiceItemIds().isEmpty())) {
            throw new AppException(ErrorCode.MUST_SELECT_SERVICE);
        }

        appointment.setCustomerUser(customer);
        appointment.setVehicle(vehicle);
        appointment.setStatus(AppointmentStatus.PENDING);

        BigDecimal totalCost = BigDecimal.ZERO;
        Set<Long> packageServiceItemIds;
        Long modelId = vehicle.getModel().getId();

        // Xử lý gói dịch vụ nếu được chọn
        if(request.getServicePackageId() != null) {
            ServicePackage servicePackage = servicePackageRepository.findById(request.getServicePackageId())
                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));

            // Lấy các dịch vụ cụ thể cho model này và gói dịch vụ này
            List<ModelPackageItem> modelItems = modelPackageItemRepository
                    .findByVehicleModelIdAndServicePackageId(modelId, request.getServicePackageId());

//            if (modelItems.isEmpty()) {
//                throw new AppException(ErrorCode.SERVICE_PACKAGE_NOT_AVAILABLE_FOR_MODEL);
//            }

            // Trích xuất service items từ modelItems
            List<ServiceItem> packageItems = modelItems.stream()
                    .map(ModelPackageItem::getServiceItem)
                    .collect(Collectors.toList());

            // Lưu ID của các dịch vụ trong gói để kiểm tra trùng lặp sau này
            packageServiceItemIds = packageItems.stream()
                    .map(ServiceItem::getId)
                    .collect(Collectors.toSet());

            // Tính tổng chi phí gói dịch vụ dựa trên model xe
            BigDecimal packageCost = modelItems.stream()
                    .map(ModelPackageItem::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            appointment.setServicePackage(servicePackage);

            // Thêm các service items vào appointment
            if (appointment.getServiceItems() == null) {
                appointment.setServiceItems(new ArrayList<>(packageItems));
            } else {
                appointment.getServiceItems().addAll(packageItems);
            }

            totalCost = totalCost.add(packageCost);
        } else {
            packageServiceItemIds = new HashSet<>();
        }

        // Xử lý dịch vụ lẻ nếu được chọn
        if(request.getServiceItemIds() != null && !request.getServiceItemIds().isEmpty()) {
            // Validate: Dịch vụ tồn tại
            List<ServiceItem> serviceItems = serviceItemRepository.findAllByIdIn(request.getServiceItemIds());
            if(serviceItems.size() != request.getServiceItemIds().size()) {
                throw new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND);
            }

            // Validate: Không trùng lặp với dịch vụ trong gói
            if (!packageServiceItemIds.isEmpty()) {
                // Kiểm tra xem có dịch vụ lẻ nào đã có trong gói không
                List<ServiceItem> duplicateItems = serviceItems.stream()
                        .filter(item -> packageServiceItemIds.contains(item.getId()))
                        .collect(Collectors.toList());

                if (!duplicateItems.isEmpty()) {
                    throw new AppException(ErrorCode.DUPLICATE_SERVICE_ITEMS);
                }
            }

            // Tính chi phí dịch vụ lẻ theo model xe
            BigDecimal serviceItemsTotal = BigDecimal.ZERO;
            for (ServiceItem item : serviceItems) {
                // Tìm giá theo model (dịch vụ lẻ có service_package_id là NULL)
                Optional<ModelPackageItem> modelItem = modelPackageItemRepository
                        .findByVehicleModelIdAndServicePackageIsNullAndServiceItemId(modelId, item.getId());

                BigDecimal itemPrice;

                itemPrice = modelItem.get().getPrice();


                serviceItemsTotal = serviceItemsTotal.add(itemPrice);
            }

            // Thêm dịch vụ lẻ vào appointment
            if (appointment.getServiceItems() == null) {
                appointment.setServiceItems(new ArrayList<>(serviceItems));
            } else {
                appointment.getServiceItems().addAll(serviceItems);
            }

            totalCost = totalCost.add(serviceItemsTotal);
        }

        appointment.setEstimatedCost(totalCost);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Truyền repository làm context
        AppointmentResponse response = appointmentMapper.toAppointmentResponse(savedAppointment);
        appointmentMapper.mapServiceItems(savedAppointment, response, modelPackageItemRepository);

        return response;
    }


    // Admin role
    @Override
    public List<AppointmentResponse> getAppointmentByCustomerId(Long customerId) {
        userRepository.findByIdAndRoleName(customerId, PredefinedRole.CUSTOMER);
        List<Appointment> appointments = appointmentRepository.findByCustomerUserId(customerId);
        return appointments.stream()
                .map(appointmentMapper::toAppointmentResponse)
                .toList();
    }

    // Admin role
    @Override
    public List<AppointmentResponse> getAppointmentByVehicleId(Long vehicleId) {
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        List<Appointment> appointments = appointmentRepository.findByVehicleId(vehicleId);

        return appointments.stream()
                .map(appointmentMapper::toAppointmentResponse)
                .toList();
    }

    // Admin role
    @Override
    public AppointmentResponse getAppointmentByAppointmentId(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    // Admin role
    @Override
    public List<AppointmentResponse> getAppointmentByStatus(AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByStatus(status);
        return appointments.stream()
                .map(appointmentMapper::toAppointmentResponse)
                .toList();
    }

    // Admin role
    @Override
    public List<AppointmentResponse> getAppointmentByTechnicianId(Long technicianId) {
        userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Appointment> appointments = appointmentRepository.findByTechnicianUserId(technicianId);

        return appointments.stream()
                .map(appointmentMapper::toAppointmentResponse)
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
                .map(appointmentMapper::toAppointmentResponse)
                .toList();
    }
    @Override
    public AppointmentResponse setStatusAppointment(Long id, AppointmentUpdateRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // Update status
        if (request.getStatus() != null) {
            appointment.setStatus(request.getStatus());
        }

        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentUpdateRequest appointment) {
        return null;
    }

    @Override
    public AppointmentResponse cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // Validate: Không thể hủy appointment đã hoàn thành
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppException(ErrorCode.CANNOT_CANCEL_COMPLETED_APPOINTMENT);
        }

        // Validate: Không thể hủy appointment đã bị hủy
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new AppException(ErrorCode.APPOINTMENT_ALREADY_CANCELLED);
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }
//    @Override
//    public AppointmentResponse createAppointment(AppointmentRegistrationRequest request) {
//        Appointment appointment = appointmentMapper.toAppointment(request);
//        User customer = customerRepository.findById(request.getCustomerId())
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//        appointment.setCustomerUser(customer);
//
//        User technician = technicianRepository.findById(request.getTechnicianId())
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//        appointment.setTechnicianUser(technician);
//
//        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
//                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
//
//        appointment.setVehicle(vehicle);
//
//        AppointmentStatus status;
//        try {
//            status = AppointmentStatus.valueOf(request.getStatus().name());
//        } catch (IllegalArgumentException e) {
//            throw new AppException(ErrorCode.STATUS_INVALID);
//        }
//        boolean vehicleBooked = appointmentRepository.existsByVehicleIdAndAppointmentDate(request.getVehicleId(), request.getAppointmentDate());
//        if (vehicleBooked) {
//            throw new AppException(ErrorCode.APPOINTMENT_EXISTED);
//        }
//        boolean exist = appointmentRepository.existsByTechnicianUserIdAndAppointmentDate(request.getTechnicianId(), request.getAppointmentDate());
//        if (exist) {
//            throw new AppException(ErrorCode.APPOINTMENT_EXISTED);
//        }
//
//        appointment = appointmentRepository.save(appointment);
//        return appointmentMapper.toAppointmentResponse(appointment);
//        return null;
//    }


//    @Override
//    public List<AppointmentResponse> getAppointmentByCustomerId(long customerId) {
//        List<Appointment> list = appointmentRepository.getAppointmentByCustomerUser_Id(customerId);
//        if (list.isEmpty()) {
//            throw new AppException(ErrorCode.USER_NOT_FOUND);
//        }
//        return list.stream()
//                .map(appointmentMapper::toAppointmentResponse)
//                .toList();
//    }
//
//
//    @Override
//    public Optional<AppointmentResponse> getAppointmentByAppointmentId(long appointmentId) {
//        return appointmentRepository.findById(appointmentId)
//                .map(appointmentMapper::toAppointmentResponse);
//    }
//
//    @Override
//    public List<AppointmentResponse> getAll() {
//        return appointmentRepository.findAll().stream()
//                .map(appointmentMapper::toAppointmentResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<AppointmentResponse> getAppointmentByStatus(AppointmentStatus status) {
//        return appointmentRepository.findByStatus(status).stream()
//                .map(appointmentMapper::toAppointmentResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<AppointmentResponse> getTechnicianByTechnicianIdAndSchedule(long technicianId, LocalDateTime scheduleDate) {
//        return appointmentRepository.findByTechnicianUserIdAndAppointmentDate(technicianId, scheduleDate).stream()
//                .map(appointmentMapper::toAppointmentResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public AppointmentResponse updateAppointment(Long appointmentId, AppointmentUpdateRequest request) {
//        Appointment appointment = appointmentRepository.findById(appointmentId)
//                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
//
//        User customer = customerRepository.findById(request.getCustomerId())
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//        appointment.setCustomerUser(customer);
//
//        User technician = technicianRepository.findById(request.getTechnicianId())
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//        appointment.setTechnicianUser(technician);
//
//        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
//                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
//        appointment.setVehicle(vehicle);
//
//        appointment.setAppointmentDate(request.getAppointmentDate());
//        appointment.setNotes(request.getNotes());
//        appointment.setStatus(AppointmentStatus.valueOf(request.getStatus()));
//
//        appointmentMapper.updateAppointment(request,appointment);
//
//        Appointment updated = appointmentRepository.save(appointment);
//
//        return appointmentMapper.toAppointmentResponse(updated);
//    }
//
//
//    @Override
//    public AppointmentResponse assignTechnician(long appointmentId, long technicianId) {
//        Appointment appointment = appointmentRepository.findById(appointmentId)
//                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
//        User technician = technicianRepository.findById(technicianId)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//
//        if (!isTechnicianAvailable(technicianId, appointment.getAppointmentDate())) {
//            throw new AppException(ErrorCode.TECHNICIAN_NOT_AVAILABLE);
//        }
//
//        appointment.setTechnicianUser(technician);
//        appointment.setStatus(AppointmentStatus.CONFIRMED);
//        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
//    }
//
//    @Override
//    public AppointmentResponse cancelAppointment(long appointmentId) {
//        Appointment appointment = appointmentRepository.findById(appointmentId)
//                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
//        appointment.setStatus(AppointmentStatus.CANCELLED);
//        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
//    }
//
//    @Override
//    public boolean isTechnicianAvailable(long technicianId, LocalDateTime scheduleDate) {
//        return !appointmentRepository.existsByTechnicianUserIdAndAppointmentDate(technicianId, scheduleDate);
//    }
}
