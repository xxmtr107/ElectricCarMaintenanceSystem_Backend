package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModelPackageItemRequest {

    @NotNull(message = "NOT_BLANK")
    Long vehicleModelId;

    @NotNull(message = "NOT_BLANK")
    Integer milestoneKm;

    @NotNull(message = "NOT_BLANK")
    Long serviceItemId;

    @NotNull(message = "NOT_BLANK")
    @DecimalMin(value = "0.01", message = "PRICE_INVALID")
    BigDecimal price;
}