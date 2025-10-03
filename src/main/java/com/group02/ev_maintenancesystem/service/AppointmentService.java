package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.AppointmentRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.AppointmentUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;
public interface AppointmentService {
    AppointmentResponse createAppointment(AppointmentRegistrationRequest appointment);
    List<AppointmentResponse>getAppointmentByCustomerId(long customerId);
    Optional<AppointmentResponse> getAppointmentByAppointmentId(long appointmentId);
    List<AppointmentResponse>getAll();
    List<AppointmentResponse> getAppointmentByStatus(AppointmentStatus status);
    List<AppointmentResponse>getTechnicianByTechnicianIdAndSchedule(long technicianId, LocalDateTime appointmentDate);
    AppointmentResponse updateAppointment(Long id,AppointmentUpdateRequest appointment);
    AppointmentResponse assignTechnician(long appointmentId,long technicianId);
    AppointmentResponse cancelAppointment(long appointmentId);
    boolean isTechnicianAvailable(long technicianId, LocalDateTime scheduleDate);
}
