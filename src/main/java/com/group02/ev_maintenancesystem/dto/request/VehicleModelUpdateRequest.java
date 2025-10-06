package com.group02.ev_maintenancesystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModelUpdateRequest {
    private String name;
    String modelYear;
    Integer basicMaintenance;
    Integer comprehensiveMaintenance;
    Integer basicMaintenanceTime;
    Integer comprehensiveMaintenanceTime;
}
