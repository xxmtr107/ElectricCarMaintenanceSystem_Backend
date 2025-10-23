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
    // Create appointment by customer
    AppointmentResponse createAppointmentByCustomer(Authentication authentication, CustomerAppointmentRequest request);
    // Get appointments by customer ID
    List<AppointmentResponse>getAppointmentByCustomerId(Long customerId);
    // Get appointments by vehicle ID
    List<AppointmentResponse>getAppointmentByVehicleId(Long vehicleId);
    // Get appointments by technician ID
    List<AppointmentResponse>getAppointmentByTechnicianId(Long technicianId);
    // Get appointment by appointment ID
    AppointmentResponse getAppointmentByAppointmentId(Long appointmentId);
    // Get all appointments
    List<AppointmentResponse> getAll();
    // Get appointments by status
    List<AppointmentResponse> getAppointmentByStatus(AppointmentStatus status);
    // Update appointment
    AppointmentResponse updateAppointment(Long id,AppointmentUpdateRequest appointment);
    // Cancel appointment
    AppointmentResponse cancelAppointment(Long appointmentId);
    // Get appointments between dates
    List<AppointmentResponse> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    // Assign technician to appointment
    AppointmentResponse assignTechnician(Long appointmentId, Long technicianId);

    AppointmentResponse setStatusAppointment(Long id, AppointmentStatus newStatus);


//    boolean isTechnicianAvailable(long technicianId, LocalDateTime scheduleDate);
}
