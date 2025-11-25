package com.group02.ev_maintenancesystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.group02.ev_maintenancesystem.enums.GeneralStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse {
    Long id;
    String licensePlate;
    String vin;
    Integer currentKm;
    @JsonFormat(pattern = "yyyy-MM")
    LocalDate purchaseYear;
    Long modelId;
    Long customerId;
    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String createdBy;
    String updatedBy;
}
