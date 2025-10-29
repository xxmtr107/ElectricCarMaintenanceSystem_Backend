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

    @NotNull(message = "STATUS_INVALID")
    AppointmentStatus status;


}
