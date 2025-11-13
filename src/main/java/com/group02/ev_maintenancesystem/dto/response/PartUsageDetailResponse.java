package com.group02.ev_maintenancesystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO này đại diện cho MỘT phụ tùng đã sử dụng, phân biệt
 * giữa "INCLUDED" (trọn gói, giá 0) và "EXTRA" (phát sinh, có giá).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartUsageDetailResponse {
    Long id; // ID của PartUsage (sẽ là null đối với phụ tùng trọn gói)
    Integer quantityUsed;
    BigDecimal totalPrice; // Sẽ là 0.00 đối với phụ tùng trọn gói
    Long sparePartId;
    String sparePartName;
    String sparePartNumber;
    Long maintenanceRecordId;
    LocalDateTime createdAt;
    String usageType; // "INCLUDED" hoặc "EXTRA"
}