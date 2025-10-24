package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.response.MaintenanceRecordResponse;
import com.group02.ev_maintenancesystem.entity.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceRecordService {
    void createMaintenanceRecord(Appointment appointment);

    MaintenanceRecordResponse getByMaintenanceRecordId(long MaintenanceRecordId);

    List<MaintenanceRecordResponse> getAll();

    List<MaintenanceRecordResponse> findByCustomerId(long customerId);

    List<MaintenanceRecordResponse>findMaintenanceRecordsByVehicleId(long vehicleId);

    List<MaintenanceRecordResponse>findByTechnicianUserId(long technicianId);

    List<MaintenanceRecordResponse> findByAppointment_AppointmentDateBetween(LocalDateTime start, LocalDateTime end);

}
