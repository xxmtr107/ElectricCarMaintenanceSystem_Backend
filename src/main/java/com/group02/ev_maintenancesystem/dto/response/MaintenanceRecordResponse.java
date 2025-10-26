package com.group02.ev_maintenancesystem.dto.response;

import com.group02.ev_maintenancesystem.entity.ServiceItem;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaintenanceRecordResponse {
    Long id;
    int odometer;
    LocalDateTime performedAt;

    Long appointmentId;
    Long customerId;
    String customerName;


    Long servicePackageId;
    String servicePackageName;

    Long technicianId;
    String technicianName;

    Long vehicleId;
    String vehicleLicensePlate;
    String vehicleModel;

    String notes;
    List<ServiceItem> serviceItems;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
