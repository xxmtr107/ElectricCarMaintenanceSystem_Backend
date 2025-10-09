package com.group02.ev_maintenancesystem.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
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
public class MaintenanceRecordRegistrationRequest {
    @NotNull(message = "ODOMETER_INVALID")
    int odometer;

    @NotNull(message = "APPOINTMENT_NOTFOUND")
    long appointmentId;

    @NotNull(message = "SERVICE_PACKAGE_NOTFOUND")
    long servicePackageID;

    @NotNull(message = "TECHNICIAN_NOTFOUND")
    long technicianId;

    @NotNull(message = "VEHICLE_NOTFOUND")
    long vehicleId;

    String notes;

    @NotNull(message = "SERVICE_TYPE_NOTFOUND")
    List<Long> serviceTypeID;

    @Future(message = "DATE_INVALID")
    @NotNull(message = "NOT_BLANK")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime performedAt;
}
