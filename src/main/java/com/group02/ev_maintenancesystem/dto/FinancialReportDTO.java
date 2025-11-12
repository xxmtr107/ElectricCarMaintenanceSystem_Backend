package com.group02.ev_maintenancesystem.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class FinancialReportDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long serviceCenterId; // Null nếu là Admin
    private Long totalCompletedAppointments;
    private Long totalPaidInvoices;
    private BigDecimal totalRevenue; // Tổng tiền từ các hóa đơn đã thanh toán
}