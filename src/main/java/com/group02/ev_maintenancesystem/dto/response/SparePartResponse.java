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
public class SparePartResponse {
    Long id;
    String partNumber;
    String name;
    BigDecimal unitPrice;
    Integer quantityInStock;
    Integer minimumStockLevel;
    String categoryName;
    String categoryCode;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String createdBy;
    String updatedBy;
}
