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
    @Pattern(regexp = "^[0-9]{1,6}$", message ="KILOMETER_LONG_INVALID")
    private Integer currentKm;


}
