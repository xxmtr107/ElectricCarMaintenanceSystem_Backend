package com.group02.ev_maintenancesystem.dto.request;

import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceItemUpgradeRequest {

    @NotNull(message = "NOT_BLANK")
    Long serviceItemId; // ID của ServiceItem (ví dụ: 1 = "Lọc gió cabin")

    @NotNull(message = "NOT_BLANK")
    MaintenanceActionType newActionType; // (Phải là REPLACE)

    String notes; // Ghi chú của Technician
}