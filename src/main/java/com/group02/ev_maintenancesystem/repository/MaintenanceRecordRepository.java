package com.group02.ev_maintenancesystem.repository;

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
    List<MaintenanceRecord> findByVehicle_Id(long vehicleId);
    List<MaintenanceRecord> findByAppointment_TechnicianUser_Id(long technicianId);
    List<MaintenanceRecord> findByAppointment_AppointmentDateBetween(LocalDateTime start, LocalDateTime end);
    MaintenanceRecord findByAppointment_Id(long appointmentId);
    List<MaintenanceRecord> findByVehicle_IdOrderByPerformedAtDesc(Long vehicleId);
}
