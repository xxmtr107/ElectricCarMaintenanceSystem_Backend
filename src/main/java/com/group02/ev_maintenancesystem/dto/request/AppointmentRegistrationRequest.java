package com.group02.ev_maintenancesystem.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.group02.ev_maintenancesystem.configuration.CustomLocalDateTimeDeserializer;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentRegistrationRequest {

    @NotNull(message = "APPOINTMENT_DATE_INVALID")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    LocalDateTime appointmentDate;

    LocalDateTime createAt;

    @NotNull(message = "STATUS_INVALID")
    AppointmentStatus status;

    @Min(value = 1, message = "CUSTOMER_INVALID")
    long customerId;

    @Min(value = 1, message = "TECHNICIAN_INVALID")
    long technicianId;

    @Min(value = 1, message = "VEHICLE_INVALID")
    long vehicleId;

    @NotNull(message = "NOTES_INVALID")
    @Size(max = 200, message = "NOTES_TOO_LONG")
    String notes;

    @NotNull(message = "CREATOR_INVALID")
    @Size(max = 200, message = "CREATOR_TOO_LONG")
    String createBy;

    @NotNull(message = "UPDATER_INVALID")
    @Size(max = 200, message = "UPDATER_TOO_LONG")
    String updateBy;
}
