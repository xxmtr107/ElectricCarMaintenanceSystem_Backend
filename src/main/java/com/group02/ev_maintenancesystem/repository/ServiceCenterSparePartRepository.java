package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceCenterSparePartRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByServiceCenterIdAndSparePartId(Long centerId, Long sparePartId);
}