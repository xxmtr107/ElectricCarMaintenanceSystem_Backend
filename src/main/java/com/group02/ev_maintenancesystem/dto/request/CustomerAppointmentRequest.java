package com.group02.ev_maintenancesystem.dto.request;



import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CustomerAppointmentRequest {

    @Future(message = "DATE_INVALID")
    @NotNull(message = "NOT_BLANK")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime appointmentDate;

    @NotNull(message = "VEHICLE_NOT_FOUND")
    Long vehicleId;


}
