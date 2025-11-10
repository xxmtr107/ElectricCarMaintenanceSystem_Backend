package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.dto.request.PartUsageRequest;
import com.group02.ev_maintenancesystem.dto.response.PartUsageResponse;
import com.group02.ev_maintenancesystem.entity.MaintenanceRecord;
import com.sun.tools.javac.Main;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord,Long> {


    List<MaintenanceRecord> findByAppointment_CustomerUser_Id(long customerId);
    List<MaintenanceRecord> findByAppointment_Vehicle_Id(long vehicleId);
    List<MaintenanceRecord> findByAppointment_TechnicianUser_Id(long technicianId);
    List<MaintenanceRecord> findByAppointment_AppointmentDateBetween(LocalDateTime start, LocalDateTime end);
    MaintenanceRecord findByAppointment_Id(long appointmentId);
    List<MaintenanceRecord> findByAppointment_Vehicle_IdOrderByPerformedAtDesc(Long vehicleId);

    List<MaintenanceRecord> findByAppointment_AppointmentDateBetweenAndAppointment_ServiceCenter_Id(LocalDateTime start, LocalDateTime end, Long centerId);

    List<MaintenanceRecord> findByAppointment_ServiceCenter_Id(Long centerId);
}

