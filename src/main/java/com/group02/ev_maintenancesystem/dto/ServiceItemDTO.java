package com.group02.ev_maintenancesystem.dto;

import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ServiceItemDTO {
    private Long id;
    private String name;
    private String description;
}
