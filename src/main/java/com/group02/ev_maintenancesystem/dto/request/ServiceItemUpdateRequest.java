package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceItemUpdateRequest {
    @Size(min = 2, max = 50, message = "SERVICE_ITEM_NAME_INVALID")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s\\-/,:]+$", message="SERVICE_NAME_INVALID")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "SERVICE_ITEM_PRICE_INVALID")
    private BigDecimal price;

    private String description;
}
