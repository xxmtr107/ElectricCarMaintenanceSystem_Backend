package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.ServiceCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ServiceCenterRepository extends JpaRepository<ServiceCenter,Long> {

}
