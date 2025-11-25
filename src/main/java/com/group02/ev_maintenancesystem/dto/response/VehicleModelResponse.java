package com.group02.ev_maintenancesystem.dto.response;

import com.group02.ev_maintenancesystem.enums.GeneralStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleModelResponse {
    Long id;
    String name;
    String modelYear;
    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String createdBy;
    String updatedBy;

}
