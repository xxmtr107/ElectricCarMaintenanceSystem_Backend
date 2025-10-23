package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCreationRequest {
    @NotBlank(message = "NOT_BLANK")
    @Pattern(regexp = "^\\d{2}[A-Z]{2}-\\d{5}$", message = "LICENSE_PLATE_INVALID")
    private String licensePlate;

    @NotBlank(message="NOT_BLANK")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "VIN_LONG_INVALID")
    private String vin;

    @NotNull(message = "NOT_BLANK")
    //@Pattern(regexp = "^[0-9]{1,6}$", message = "KILOMETER_LONG_INVALID")
    @Min(value = 0, message = "KILOMETER_INVALID_RANGE")
    @Max(value =999999, message ="KILOMETER_INVALID_RANGE")
    private Integer currentKm;

    @NotNull(message = "NOT_BLANK")
    LocalDate purchaseYear;

    @NotNull(message = "NOT_BLANK")
    private Long modelId;

    @NotNull(message = "NOT_BLANK")
    private Long customerId;




}
