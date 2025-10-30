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
public class SparePartCreateRequest {
    @NotBlank(message = "NOT_BLANK")
    String partNumber;

    @NotBlank(message = "NOT_BLANK")
    String name;

    @NotNull(message = "NOT_BLANK")
    @DecimalMin(value = "0.0", inclusive = false, message = "SPARE_PART_PRICE_MUST_BE_POSITIVE")
    BigDecimal unitPrice;


    @NotNull(message = "NOT_BLANK")
    @Min(value = 0, message = "SPARE_PART_MIN_STOCK_CANNOT_BE_NEGATIVE")
    Integer quantityInStock;

    @NotNull(message = "NOT_BLANK")
    @Min(value = 0, message = "SPARE_PART_STOCK_CANNOT_BE_NEGATIVE")
    Integer minimumStockLevel;

    @NotNull(message = "NOT_BLANK")
    Long categoryId;
}
