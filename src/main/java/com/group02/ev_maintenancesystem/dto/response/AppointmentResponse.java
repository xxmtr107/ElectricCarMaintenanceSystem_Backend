package com.group02.ev_maintenancesystem.dto.response;

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

    long id;
    LocalDateTime createAt;
    String createBy;
    LocalDateTime updateAt;
    String updateBy;
    LocalDateTime appointmentDate;
    String notes;
    String status;
    long customerId;
    long technicianId;
    long vehicleId;
}
