package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleUpdateRequest {

    @NotNull(message = "Current KM is required")
    @Min(value = 0, message = "Kilometers must be >= 0")
    @Max(value = 999999, message = "Kilometers must be <= 999,999")
    private Integer currentKm;


}
