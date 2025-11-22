package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Find by customer
    List<Appointment> findByCustomerUserIdOrderByCreatedAtDesc(Long customerId);

    // Find by technician
    List<Appointment> findByTechnicianUserIdOrderByCreatedAtDesc(Long technicianId);

    // Find by vehicle
    List<Appointment> findByVehicleIdOrderByCreatedAtDesc(Long vehicleId);

    // Find by status
    List<Appointment> findByStatusOrderByCreatedAtDesc(AppointmentStatus status);

    // Find appointments between dates
    List<Appointment> findByAppointmentDateBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    // Check vehicle appointment conflict (Giữ nguyên hoặc thêm sort nếu cần hiển thị)
    List<Appointment> findByVehicleIdAndAppointmentDateBetweenOrderByCreatedAtDesc(
            Long vehicleId,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );

    // Find by technician and date
    List<Appointment> findByTechnicianUserIdAndAppointmentDateOrderByCreatedAtDesc(Long technicianId, LocalDateTime scheduleDate);

    List<Appointment> deleteByVehicleId(Long vehicleId); // Delete không cần sort

    List<Appointment> findByAppointmentDateOrderByCreatedAtDesc(LocalDateTime date);

    List<Appointment> findByStatusAndServiceCenterIdOrderByCreatedAtDesc(AppointmentStatus status, Long centerId);

    List<Appointment> findAllByServiceCenterIdOrderByCreatedAtDesc(Long centerId);

    List<Appointment> findByAppointmentDateBetweenAndServiceCenterIdOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate, Long centerId);
}