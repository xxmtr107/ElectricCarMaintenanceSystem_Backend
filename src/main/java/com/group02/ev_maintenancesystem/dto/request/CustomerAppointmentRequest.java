package com.group02.ev_maintenancesystem.dto.request;



import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

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
    @Schema(example = "2025-11-01 14:36")
    LocalDateTime appointmentDate;

    @NotNull(message = "VEHICLE_NOT_FOUND")
    Long vehicleId;

    @NotNull(message = "SERVICE_CENTER_NOT_FOUND") // Yêu cầu khách hàng phải chọn trung tâm
    Long centerId;

    @Min(value = 0, message = "KILOMETER_INVALID_RANGE")
    Integer currentOdo;
}
