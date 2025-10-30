package com.group02.ev_maintenancesystem.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group02.ev_maintenancesystem.entity.ServiceItem;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
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
    String technicianSpecialization;

    Long vehicleId;
    String vehicleLicensePlate;
    String vehicleModel;

    BigDecimal estimatedCost;
    Long servicePackageId;
    String servicePackageName;


    String nameCenter;
    String addressCenter;
    String districtCenter;

    // DTO nhỏ cho service items với giá theo model
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ServiceItemDTO {
        Long id;
        String name;
        String description;
        BigDecimal price;
    }

    List<ServiceItemDTO> serviceItems;


    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String createdBy;
    String updatedBy;
}
