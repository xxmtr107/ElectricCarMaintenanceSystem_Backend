package com.group02.ev_maintenancesystem.dto;

import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ModelPackageItemDTO {
    private ServiceItemDTO serviceItem; // Dùng DTO con
    private BigDecimal price;
    private MaintenanceActionType actionType;
}