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

    @NotBlank(message = "NOT_BLANK")
    String categoryName;

    @NotBlank(message = "NOT_BLANK")
    String categoryCode;

}