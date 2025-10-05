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
    @Pattern(regexp = "^VF[e]?\\d{1,2}$", message = "MODEL_NAME_INVALID")
    private String name;

    @NotBlank(message="NOT_BLANK")
    @Pattern(regexp = "202[1-5]", message = "MODEL_YEAR_INVALID")
    String modelYear;

    @NotNull(message = "NOT_BLANK")
    @Min(value = 1000, message = "BASIC_MAINTENANCE_INVALID")
    @Max(value= 19999, message = "BASIC_MAINTENANCE_INVALID")
    Integer basicMaintenance;

    @NotNull(message = "NOT_BLANK")
    @Min(value = 20000, message = "COMPREHENSIVE_MAINTENANCE_INVALID")
    @Max(value= 999999, message = "COMPREHENSIVE_MAINTENANCE_INVALID")
    Integer comprehensiveMaintenance;

    @NotNull(message = "NOT_BLANK")
    @Min(value = 1, message = "BASIC_MAINTENANCE_TIME_INVALID")
    @Max(value = 6, message = "BASIC_MAINTENANCE_TIME_INVALID")
    Integer basicMaintenanceTime;

    @NotNull(message = "NOT_BLANK")
    @Min(value = 7, message="COMPREHENSIVE_MAINTENANCE_TIME_INVALID")
    @Max(value = 12, message = "COMPREHENSIVE_MAINTENANCE_TIME_INVALID")
    Integer comprehensiveMaintenanceTime;



}
