package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.AppointmentServiceItemDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentServiceItemDetailRepository extends JpaRepository<AppointmentServiceItemDetail, Long> {
    // Tìm chi tiết dịch vụ cụ thể trong một lịch hẹn
    Optional<AppointmentServiceItemDetail> findByAppointmentIdAndServiceItemId(Long appointmentId, Long serviceItemId);
}