package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartUsageRequest {

    @NotNull(message = "NOT_BLANK")
    Long sparePartId;

    @NotNull(message = "NOT_BLANK")
    @Min(value = 1, message = "STOCK_QUANTITY_INVALID")
    Integer quantityUsed;
}