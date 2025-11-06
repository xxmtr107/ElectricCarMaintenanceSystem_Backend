package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.PartUsageRequest;
import com.group02.ev_maintenancesystem.dto.response.MaintenanceRecordResponse;
import com.group02.ev_maintenancesystem.dto.response.PartUsageResponse;
import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.entity.MaintenanceRecord;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceRecordService {
    void createMaintenanceRecord(Appointment appointment);

    MaintenanceRecordResponse getByMaintenanceRecordId(long MaintenanceRecordId);

    public List<MaintenanceRecordResponse> getAll(Authentication authentication);

    List<MaintenanceRecordResponse> findByCustomerId(long customerId);

    List<MaintenanceRecordResponse>findMaintenanceRecordsByVehicleId(long vehicleId);

    List<MaintenanceRecordResponse>findByTechnicianUserId(long technicianId);

    List<MaintenanceRecordResponse> findByAppointment_AppointmentDateBetween(LocalDateTime start, LocalDateTime end, Authentication authentication);

    public PartUsageResponse addPartToRecord(Long recordId, PartUsageRequest request, Authentication authentication);


}
