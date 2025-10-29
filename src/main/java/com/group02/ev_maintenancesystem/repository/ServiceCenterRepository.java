package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.ServiceCenter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceCenterRepository extends JpaRepository<ServiceCenter, Integer> {
    ServiceCenter findServiceCenterByAppointments_Id(long id);
}
