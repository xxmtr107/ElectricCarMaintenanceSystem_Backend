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
    private String modelYear;

    @NotNull(message = "NOT_BLANK")
    @Min(value = 1000, message = "BASIC_MAINTENANCE_INVALID")
    @Max(value = 19999, message = "BASIC_MAINTENANCE_INVALID")
    private Integer basicCycleKm;

    @NotNull(message = "NOT_BLANK")
    @Min(value = 1, message = "BASIC_MAINTENANCE_TIME_INVALID")
    @Max(value = 6, message = "BASIC_MAINTENANCE_TIME_INVALID")
    private Integer basicCycleMonths;

    @NotNull(message = "NOT_BLANK")
    @Min(value = 20000, message = "COMPREHENSIVE_MAINTENANCE_INVALID")
    @Max(value = 999999, message = "COMPREHENSIVE_MAINTENANCE_INVALID")
    private Integer standardCycleKm;

    @NotNull(message = "NOT_BLANK")
    @Min(value = 7, message="COMPREHENSIVE_MAINTENANCE_TIME_INVALID")
    @Max(value = 12, message = "COMPREHENSIVE_MAINTENANCE_TIME_INVALID")
    private Integer standardCycleMonths;

    // Premium cycle
    @NotNull(message = "NOT_BLANK")
    @Min(value = 25000, message = "PREMIUM_MAINTENANCE_INVALID")
    @Max(value = 999999, message = "PREMIUM_MAINTENANCE_INVALID")
    private Integer premiumCycleKm;

    @NotNull(message = "NOT_BLANK")
    @Min(value = 13, message = "PREMIUM_MAINTENANCE_TIME_INVALID")
    @Max(value = 60, message = "PREMIUM_MAINTENANCE_TIME_INVALID")
    private Integer premiumCycleMonths;

    // Battery cycle
    @NotNull(message = "NOT_BLANK")
    @Min(value = 48000, message = "BATTERY_MAINTENANCE_INVALID")
    @Max(value = 999999, message = "BATTERY_MAINTENANCE_INVALID")
    private Integer batteryCycleKm;

    @NotNull(message = "NOT_BLANK")
    @Min(value = 12, message = "BATTERY_MAINTENANCE_TIME_INVALID")
    @Max(value = 60, message = "BATTERY_MAINTENANCE_TIME_INVALID")
    private Integer batteryCycleMonths;
}
