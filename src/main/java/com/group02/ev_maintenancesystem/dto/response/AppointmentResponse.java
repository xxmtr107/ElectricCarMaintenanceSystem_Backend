package com.group02.ev_maintenancesystem.dto.response;

import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentResponse {

    Long id;
    LocalDateTime appointmentDate;
    String notes;
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


    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
