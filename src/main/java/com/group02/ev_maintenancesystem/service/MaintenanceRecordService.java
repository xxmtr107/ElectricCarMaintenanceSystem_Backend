package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.MaintenanceRecordRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.MaintenanceRecordUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.MaintenanceRecordResponse;
import com.group02.ev_maintenancesystem.entity.MaintenanceRecord;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceRecordService {
//    List<MaintenanceRecordResponse> createMaintenanceRecord(Authentication authentication);

    MaintenanceRecordResponse getByMaintenanceRecordId(long MaintenanceRecordId);

    List<MaintenanceRecordResponse> getAll();

    MaintenanceRecordResponse findByCustomerId(long customerId);

    List<MaintenanceRecordResponse>findMaintenanceRecordsByVehicleId(long vehicleId);

    List<MaintenanceRecordResponse>findByTechnicianUserId(long technicianId);

    List<MaintenanceRecordResponse> findByAppointment_AppointmentDateBetween(LocalDateTime start, LocalDateTime end);

    void delete(long maintenanceRecordId);
}
