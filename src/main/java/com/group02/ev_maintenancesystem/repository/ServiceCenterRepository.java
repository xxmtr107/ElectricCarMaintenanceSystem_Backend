package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.ServiceCenter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCenterRepository extends JpaRepository<ServiceCenter, Long> {
    boolean existsByName(String name);
    boolean existsByPhone(String phone);
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByPhoneAndIdNot(String phone, Long id);
    Page<ServiceCenter> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}