package com.group02.ev_maintenancesystem.dto.response;

import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentServiceItemDetailResponse {
    Long id; // ID của chính record detail này
    Long serviceItemId;
    String serviceItemName;
    MaintenanceActionType actionType; // CHECK hoặc REPLACE
    BigDecimal price; // Giá thực tế
    Boolean customerApproved; // Khách hàng/Staff đã duyệt chưa
    String technicianNotes; // Ghi chú của KTV

}