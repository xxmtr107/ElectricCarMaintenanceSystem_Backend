package com.group02.ev_maintenancesystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceResponse {
    Long id;
    BigDecimal totalAmount;
    String status; // UNPAID, PAID, CANCELLED
    Long maintenanceRecordId;
    Long serviceCenterId;
    String serviceCenterName;

    // Chi tiết đầy đủ của Maintenance Record (bao gồm dịch vụ và phụ tùng)
    MaintenanceRecordResponse maintenanceRecord;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}