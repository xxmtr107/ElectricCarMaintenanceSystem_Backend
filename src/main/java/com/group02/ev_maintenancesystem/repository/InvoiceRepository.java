package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByServiceCenterId(Long centerId);

    List<Invoice> findByStatus(String status);
}
