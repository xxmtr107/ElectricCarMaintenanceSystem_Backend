package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
import com.group02.ev_maintenancesystem.dto.request.AppointmentRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerAppointmentRequest;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.entity.Vehicle;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.AppointmentMapper;
import com.group02.ev_maintenancesystem.repository.AppointmentRepository;
import com.group02.ev_maintenancesystem.repository.UserRepository;
import com.group02.ev_maintenancesystem.repository.VehicleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentServiceImpl implements AppointmentService {

    AppointmentRepository appointmentRepository;
    UserRepository userRepository;
    AppointmentMapper appointmentMapper;
    VehicleRepository vehicleRepository;

    @Override
    public AppointmentResponse createAppointmentByCustomer(Authentication authentication, CustomerAppointmentRequest request) {
        Appointment appointment = appointmentMapper.toAppointmentCustomer(request);

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long customerId = jwt.getClaim("userId");

        User customer = userRepository.findByIdAndRoleName(customerId, PredefinedRole.CUSTOMER)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        if(!vehicle.getCustomerUser().getId().equals(customerId)) {
            throw new AppException(ErrorCode.VEHICLE_NOT_BELONG_TO_CUSTOMER);
        }

        // 🔥 GẮN customer + vehicle vào appointment
        appointment.setCustomerUser(customer);
        appointment.setVehicle(vehicle);
        appointment.setStatus(AppointmentStatus.PENDING);

        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Override
    public List<AppointmentResponse> getAppointmentByCustomerId(Long customerId) {
        return List.of();
    }

    @Override
    public List<AppointmentResponse> getAppointmentByVehicleId(Long vehicleId) {
        return List.of();
    }

    @Override
    public Optional<AppointmentResponse> getAppointmentByAppointmentId(Long appointmentId) {
        return Optional.empty();
    }

    @Override
    public List<AppointmentResponse> getAll() {
        return List.of();
    }

    @Override
    public List<AppointmentResponse> getAppointmentByStatus(AppointmentStatus status) {
        return List.of();
    }

    @Override
    public List<AppointmentResponse> getTechnicianByTechnicianId(Long technicianId) {
        return List.of();
    }

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentUpdateRequest appointment) {
        return null;
    }

    @Override
    public AppointmentResponse cancelAppointment(Long appointmentId) {
        return null;
    }

    @Override
    public List<AppointmentResponse> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return List.of();
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
