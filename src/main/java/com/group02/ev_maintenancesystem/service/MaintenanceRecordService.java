package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.MaintenanceRecordRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.MaintenanceRecordUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.MaintenanceRecordResponse;
import com.group02.ev_maintenancesystem.entity.MaintenanceRecord;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceRecordService {
    MaintenanceRecordResponse createMaintenanceRecord(Authentication authentication, MaintenanceRecordRegistrationRequest request);

    MaintenanceRecordResponse getByMaintenanceRecordId(long MaintenanceRecordId);

    List<MaintenanceRecordResponse> getAll();

    MaintenanceRecordResponse findByCustomerId(long customerId);

    List<MaintenanceRecordResponse>findMaintenanceRecordsByVehicleId(long vehicleId);

    List<MaintenanceRecordResponse>findByTechnicianUserId(long technicianId);

    List<MaintenanceRecordResponse> findByAppointment_AppointmentDateBetween(LocalDateTime start, LocalDateTime end);

    MaintenanceRecordResponse update(long id,MaintenanceRecordUpdateRequest request,Authentication authentication);

    void delete(long maintenanceRecordId);
}
