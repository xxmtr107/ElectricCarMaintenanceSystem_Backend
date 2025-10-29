package com.group02.ev_maintenancesystem.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Định nghĩa hành động bảo dưỡng:
 * CHECK: 'o' - Kiểm tra, điều chỉnh, vệ sinh...
 * REPLACE: '•' - Thay mới
 */
public enum MaintenanceActionType {
    CHECK,
    REPLACE;

    @JsonCreator
    public static MaintenanceActionType fromJson(String value) {
        return MaintenanceActionType.valueOf(value.trim().toUpperCase());
    }
}

