package com.group02.ev_maintenancesystem.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleUpdateRequest {
    @Min(value = 0, message = "KILOMETER_INVALID_RANGE")
    @Max(value =999999, message ="KILOMETER_INVALID_RANGE")
    private Integer currentKm;

}
