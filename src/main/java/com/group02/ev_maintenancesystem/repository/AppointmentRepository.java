package com.group02.ev_maintenancesystem.repository;


import com.group02.ev_maintenancesystem.dto.response.AppointmentResponse;
import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {



    // Find by customer
    List<Appointment> findByCustomerUserId(Long customerId);

    // Find by technician
    List<Appointment> findByTechnicianUserId(Long technicianId);

    // Find by vehicle
    List<Appointment> findByVehicleId(Long vehicleId);

    // Find by status
    List<Appointment> findByStatus(AppointmentStatus status);

    // Find appointments between dates
    List<Appointment> findByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);

    // Check vehicle appointment conflict
    List<Appointment>  findByVehicleIdAndAppointmentDateBetween(
            Long vehicleId,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );

    // Find by technician and date
    List<Appointment> findByTechnicianUserIdAndAppointmentDate(Long technicianId, LocalDateTime scheduleDate);

    List<Appointment> deleteByVehicleId(Long vehicleId);

    List<Appointment> findByAppointmentDate(LocalDateTime date);

    List<Appointment> findByStatusAndServiceCenterId(AppointmentStatus status, Long centerId);

    List<Appointment> findAllByServiceCenterId(Long centerId);

    List<Appointment> findByAppointmentDateBetweenAndServiceCenterId(LocalDateTime startDate, LocalDateTime endDate, Long centerId);
}

