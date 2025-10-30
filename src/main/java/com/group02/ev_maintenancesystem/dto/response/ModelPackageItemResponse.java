package com.group02.ev_maintenancesystem.dto.response;

import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModelPackageItemResponse {

    Long id;
    BigDecimal price;

    Long vehicleModelId;
    String vehicleModelName;

    Integer milestoneKm;

    Long serviceItemId;
    String serviceItemName;

    MaintenanceActionType actionType;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

