package com.group02.ev_maintenancesystem.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.group02.ev_maintenancesystem.configuration.CustomLocalDateTimeDeserializer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    LocalDateTime appointmentDate;
    LocalDateTime updateAt;

    @NotBlank(message = "NOT_BLANK")
    String createBy;

    @NotBlank(message = "NOT_BLANK")
    String updateBy;

    @NotBlank(message = "NOT_BLANK")
    String status;

    @NotBlank(message = "NOT_BLANK")
    String notes;

    @Min(value = 1, message = "CUSTOMER_INVALID")
    long customerId;

    @Min(value = 1, message = "TECHNICIAN_INVALID")
    long technicianId;

    @Min(value = 1, message = "VEHICLE_INVALID")
    long vehicleId;
}
