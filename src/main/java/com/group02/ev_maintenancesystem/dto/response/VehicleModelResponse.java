package com.group02.ev_maintenancesystem.dto.response;

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


}
