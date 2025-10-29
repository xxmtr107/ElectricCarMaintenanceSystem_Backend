package com.group02.ev_maintenancesystem.dto;

import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ModelPackageItemDTO {
    private ServiceItemDTO serviceItem; // Dùng DTO con
    private BigDecimal price;
    private MaintenanceActionType actionType;
}