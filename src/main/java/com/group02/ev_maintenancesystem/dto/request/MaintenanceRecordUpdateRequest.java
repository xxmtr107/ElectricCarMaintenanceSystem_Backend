package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaintenanceRecordUpdateRequest {
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
}
