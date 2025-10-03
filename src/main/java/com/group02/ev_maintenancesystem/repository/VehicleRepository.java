package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.Vehicle;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepositoryImplementation<Vehicle, Long> {
    boolean existsByVin(String vin);
    boolean existsByLicensePlate(String licensePlate);
    List<Vehicle> findByCustomerUserId(Long userId);

}
