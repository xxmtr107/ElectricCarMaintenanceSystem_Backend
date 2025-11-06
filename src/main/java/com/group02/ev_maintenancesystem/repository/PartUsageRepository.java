package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.PartUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartUsageRepository extends JpaRepository<PartUsage, Long> {
}