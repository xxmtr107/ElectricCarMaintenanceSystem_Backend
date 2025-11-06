package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.*;
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
    List<AppointmentResponse> getAll(Authentication authentication);
    // Get appointments by status
    List<AppointmentResponse> getAppointmentByStatus(AppointmentStatus status, Authentication authentication);
    // Cancel appointment
    AppointmentResponse cancelAppointment(Long appointmentId, Authentication authentication);
    // Get appointments between dates
    List<AppointmentResponse> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate, Authentication authentication);
    // Assign technician to appointment
    AppointmentResponse assignTechnician(Long appointmentId, Long technicianId, Authentication authentication);

    AppointmentResponse updateAppointment(Long id, AppointmentUpdateRequest appointment);

    AppointmentResponse setStatusAppointment(Long id, AppointmentStatus newStatus, Authentication authentication);

    AppointmentResponse approveServiceItem(Long appointmentId, ServiceItemApproveRequest request, Authentication authentication);

    AppointmentResponse upgradeServiceItem(Long appointmentId, ServiceItemUpgradeRequest request, Authentication authentication);

}
