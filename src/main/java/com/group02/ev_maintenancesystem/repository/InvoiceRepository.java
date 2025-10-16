package com.group02.ev_maintenancesystem.repository;

import com.group02.ev_maintenancesystem.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
