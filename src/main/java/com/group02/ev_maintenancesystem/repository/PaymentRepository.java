package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.dto.MonthlyRevenueDTO;
import com.group02.ev_maintenancesystem.entity.Payment;
import com.group02.ev_maintenancesystem.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionCode(String transactionCode);
    List<Payment> findPaymentByStatus(PaymentStatus paymentStatus);
}
