package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.VehicleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, Long> {
    boolean existsByName(String vehicleModelName);
    Page<VehicleModel> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
