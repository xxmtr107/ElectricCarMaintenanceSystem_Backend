package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByCustomerUserId(Long customerId);

    Optional<Appointment> findById(Long appointmentId);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByTechnicianUserIdAndAppointmentDate(Long technicianId, LocalDateTime scheduleDate);

    boolean existsByTechnicianUserIdAndAppointmentDate(Long technicianId, LocalDateTime scheduleDate);

    List<Appointment> getAppointmentByCustomerUser_Id(long customerId);

    boolean existsByVehicleIdAndAppointmentDate(long id, LocalDateTime appointmentDate);
}

