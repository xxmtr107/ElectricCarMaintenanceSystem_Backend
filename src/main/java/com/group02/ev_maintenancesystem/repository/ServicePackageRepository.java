package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {
    boolean existsByName(String name);
}