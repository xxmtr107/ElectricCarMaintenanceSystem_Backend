package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    void deleteByVehicleId(Long vehicleId);
}
