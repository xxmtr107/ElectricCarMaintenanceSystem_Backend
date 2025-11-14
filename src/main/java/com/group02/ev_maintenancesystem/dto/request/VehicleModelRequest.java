package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleModelRequest {
    @NotBlank(message = "NOT_BLANK")
    private String name;

    @NotBlank(message="NOT_BLANK")
    @Pattern(regexp = "202[1-5]", message = "MODEL_YEAR_INVALID")
    private String modelYear;

}
