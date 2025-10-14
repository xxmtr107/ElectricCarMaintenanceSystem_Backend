package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    List<ServiceItem> findAllByIdIn(List<Long> ids);
    List<ServiceItem> findByServicePackages_Id(Long id);

}