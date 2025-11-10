package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.response.MaintenanceRecordResponse;
import com.group02.ev_maintenancesystem.entity.MaintenanceRecord;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MaintenanceRecordMapper {

    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "customerId", source = "appointment.customerUser.id")
    @Mapping(target = "customerName", source = "appointment.customerUser.fullName")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")

    @Mapping(target = "technicianId", source = "appointment.technicianUser.id")
    @Mapping(target = "technicianName", source = "appointment.technicianUser.fullName")

    @Mapping(target = "vehicleId", source = "appointment.vehicle.id")
    @Mapping(target = "vehicleLicensePlate", source = "appointment.vehicle.licensePlate")
    @Mapping(target = "vehicleModel", source = "appointment.vehicle.model.name")

    @Mapping(target = "servicePackageName", source = "appointment.servicePackageName")

    @Mapping(target = "serviceItems", ignore = true)

    @Mapping(target = "partUsages", source = "partUsages")

    MaintenanceRecordResponse toMaintenanceRecordResponse(MaintenanceRecord maintenanceRecord);
}