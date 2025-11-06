package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.InvoiceResponse;
import com.group02.ev_maintenancesystem.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class InvoiceController {

    InvoiceService invoiceService;

    /**
     * Endpoint cho Staff tạo hoá đơn từ một lịch hẹn đã COMPLETED.
     */
    @PostMapping("/generate/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<InvoiceResponse> generateInvoice(
            @PathVariable Long appointmentId, Authentication authentication) {
        return ApiResponse.<InvoiceResponse>builder()
                .message("Invoice generated successfully")
                .result(invoiceService.createInvoiceForAppointment(appointmentId, authentication))
                .build();
    }

    /**
     * Endpoint cho Customer/Staff/Admin xem chi tiết hoá đơn bằng ID hoá đơn.
     * Quyền xem chi tiết được xử lý ở tầng Service.
     */
    @GetMapping("/{invoiceId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<InvoiceResponse> getInvoiceById(
            @PathVariable Long invoiceId, Authentication authentication) {
        return ApiResponse.<InvoiceResponse>builder()
                .message("Invoice fetched successfully")
                .result(invoiceService.getInvoiceById(invoiceId, authentication))
                .build();
    }

    /**
     * Endpoint cho Customer/Staff/Admin xem hoá đơn (nếu có) bằng ID lịch hẹn.
     * Quyền xem chi tiết được xử lý ở tầng Service.
     */
    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<InvoiceResponse> getInvoiceByAppointmentId(
            @PathVariable Long appointmentId, Authentication authentication) {
        return ApiResponse.<InvoiceResponse>builder()
                .message("Invoice fetched successfully")
                .result(invoiceService.getInvoiceByAppointmentId(appointmentId, authentication))
                .build();
    }

    /**
     * Endpoint cho Customer xem tất cả hoá đơn của mình.
     */
    @GetMapping("/my-invoices")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<List<InvoiceResponse>> getMyInvoices(Authentication authentication) {
        return ApiResponse.<List<InvoiceResponse>>builder()
                .message("My invoices fetched successfully")
                .result(invoiceService.getMyInvoices(authentication))
                .build();
    }

    /**
     * Endpoint cho Admin/Staff xem tất cả hoá đơn (Staff chỉ thấy ở center của mình).
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<List<InvoiceResponse>> getAllInvoices(Authentication authentication) {
        return ApiResponse.<List<InvoiceResponse>>builder()
                .message("All invoices fetched successfully")
                .result(invoiceService.getAllInvoices(authentication))
                .build();
    }
}