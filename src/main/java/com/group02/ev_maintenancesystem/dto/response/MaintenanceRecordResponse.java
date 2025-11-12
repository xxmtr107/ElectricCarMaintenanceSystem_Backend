package com.group02.ev_maintenancesystem.dto.response;

import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO;
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO;
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


    String servicePackageName;

    Long technicianId;
    String technicianName;

    Long vehicleId;
    String vehicleLicensePlate;
    String vehicleModel;

    String notes;
    List<ModelPackageItemDTO> serviceItems;

    List<PartUsageResponse> partUsages;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
