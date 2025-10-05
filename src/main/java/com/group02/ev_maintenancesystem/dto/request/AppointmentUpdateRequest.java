package com.group02.ev_maintenancesystem.dto.request;


import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentUpdateRequest {

    @Future(message = "DATE_INVALID")
    @NotNull(message = "NOT_BLANK")
    LocalDateTime appointmentDate;

    @NotNull(message = "STATUS_INVALID")
    AppointmentStatus status;

    @NotNull(message = "USER_NOT_FOUND")
    Long customerId;

    @NotNull(message = "USER_NOT_FOUND")
    Long technicianId;

    @NotNull(message = "VEHICLE_NOT_FOUND")
    Long vehicleId;

}
