package com.group02.ev_maintenancesystem.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group02.ev_maintenancesystem.dto.ModelPackageItemDTO;
import com.group02.ev_maintenancesystem.dto.ServiceItemDTO;
import com.group02.ev_maintenancesystem.entity.ServiceItem;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.LastModifiedBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentResponse {

    Long id;
    LocalDateTime appointmentDate;
    AppointmentStatus status;

    Long customerId;
    String customerName;
    String customerPhone;
    String customerEmail;

    Long technicianId;
    String technicianName;

    Long vehicleId;
    String vehicleLicensePlate;
    String vehicleModel;

    BigDecimal estimatedCost;
    Long servicePackageId;
    String servicePackageName;


    String nameCenter;
    String addressCenter;
    String districtCenter;

    Integer milestoneKm;
    List<ModelPackageItemDTO> serviceItems;


    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String createdBy;
    String updatedBy;
}
