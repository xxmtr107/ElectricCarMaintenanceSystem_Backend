package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SparePartUpdateRequest {
    String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "SPARE_PART_PRICE_MUST_BE_POSITIVE")
    BigDecimal unitPrice;

    @Min(value = 0, message = "SPARE_PART_STOCK_CANNOT_BE_NEGATIVE")
    int minimumStockLevel;

    Long categoryId;
}
