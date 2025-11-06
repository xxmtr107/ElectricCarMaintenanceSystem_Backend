package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.response.InvoiceResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface InvoiceService {

    /**
     * Staff tạo hoá đơn cho một lịch hẹn đã hoàn thành (COMPLETED)
     */
    InvoiceResponse createInvoiceForAppointment(Long appointmentId, Authentication authentication);

    /**
     * Lấy hoá đơn (nếu có) theo ID lịch hẹn (appointmentId)
     */
    InvoiceResponse getInvoiceByAppointmentId(Long appointmentId, Authentication authentication);

    /**
     * Lấy hoá đơn theo ID hoá đơn (invoiceId)
     */
    InvoiceResponse getInvoiceById(Long invoiceId, Authentication authentication);

    /**
     * Customer lấy tất cả hoá đơn của mình
     */
    List<InvoiceResponse> getMyInvoices(Authentication authentication);

    /**
     * Staff/Admin lấy tất cả hoá đơn (đã lọc theo center nếu là Staff)
     */
    List<InvoiceResponse> getAllInvoices(Authentication authentication);
}