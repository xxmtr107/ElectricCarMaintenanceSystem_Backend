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
public class SparePartResponse {
    Long id;
    String partNumber;
    String name;
    BigDecimal unitPrice;
    String categoryName;
    String categoryCode;
    // Đã xóa quantityInStock và minimumStockLevel
    GeneralStatus recordStatus;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String createdBy;
    String updatedBy;
}