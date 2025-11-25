package com.group02.ev_maintenancesystem.dto.response;

import com.group02.ev_maintenancesystem.enums.GeneralStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartUsageResponse {
    Long id;
    Integer quantityUsed;
    BigDecimal totalPrice;
    Long sparePartId;
    String sparePartName;
    String sparePartNumber;
    Long maintenanceRecordId;
    LocalDateTime createdAt;
}