package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleUpdateRequest {
    @Min(value = 0, message = "KILOMETER_INVALID_RANGE")
    @Max(value =0, message ="KILOMETER_INVALID_RANGE")
    private Integer currentKm;
}
