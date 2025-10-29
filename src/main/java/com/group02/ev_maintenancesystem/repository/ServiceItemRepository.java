package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.dto.response.ServiceItemResponse;
import com.group02.ev_maintenancesystem.entity.ServiceItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    List<ServiceItem> findAllByIdIn(List<Long> ids);
//    List<ServiceItem> findByServicePackageId(Long servicePackageId);
    Page<ServiceItem> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    boolean existsByName(String itemName);
}