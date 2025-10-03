package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
