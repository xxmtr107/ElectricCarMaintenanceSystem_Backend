package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord,Long> {

    List<MaintenanceRecord> findByAppointment_CustomerUser_IdOrderByCreatedAtDesc(long customerId);

    List<MaintenanceRecord> findByAppointment_Vehicle_IdOrderByCreatedAtDesc(long vehicleId);

    List<MaintenanceRecord> findByAppointment_TechnicianUser_IdOrderByCreatedAtDesc(long technicianId);

    List<MaintenanceRecord> findByAppointment_AppointmentDateBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    MaintenanceRecord findByAppointment_Id(long appointmentId);

    // Cái này giữ nguyên logic sort theo PerformedAt (ngày thực hiện) hay đổi sang CreatedAt tùy bạn,
    // nhưng tên hàm hiện tại là OrderByPerformedAtDesc
    List<MaintenanceRecord> findByAppointment_Vehicle_IdOrderByPerformedAtDesc(Long vehicleId);

    List<MaintenanceRecord> findByAppointment_AppointmentDateBetweenAndAppointment_ServiceCenter_IdOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end, Long centerId);

    List<MaintenanceRecord> findByAppointment_ServiceCenter_IdOrderByCreatedAtDesc(Long centerId);
}