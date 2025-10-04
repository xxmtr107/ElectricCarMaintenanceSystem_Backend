package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.AppointmentRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerAppointmentRequest;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.*;
public interface AppointmentService {
    AppointmentResponse createAppointmentByCustomer(Authentication authentication, CustomerAppointmentRequest request);
    List<AppointmentResponse>getAppointmentByCustomerId(Long customerId);
    List<AppointmentResponse>getAppointmentByVehicleId(Long vehicleId);
    Optional<AppointmentResponse> getAppointmentByAppointmentId(Long appointmentId);
    List<AppointmentResponse> getAll();
    List<AppointmentResponse> getAppointmentByStatus(AppointmentStatus status);
    List<AppointmentResponse>getTechnicianByTechnicianId(Long technicianId);
    AppointmentResponse updateAppointment(Long id,AppointmentUpdateRequest appointment);
    AppointmentResponse cancelAppointment(Long appointmentId);
    List<AppointmentResponse> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

//    AppointmentResponse assignTechnician(long appointmentId,long technicianId);

//    boolean isTechnicianAvailable(long technicianId, LocalDateTime scheduleDate);
}
