package com.group02.ev_maintenancesystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleModelUpdateResponse {
    Long id;
    String name;
    String modelYear;
    Integer basicMaintenance;
    Integer comprehensiveMaintenance;
    Integer basicMaintenanceTime;
    Integer comprehensiveMaintenanceTime;
    LocalDateTime updateAt;
    String updateBy;

}
