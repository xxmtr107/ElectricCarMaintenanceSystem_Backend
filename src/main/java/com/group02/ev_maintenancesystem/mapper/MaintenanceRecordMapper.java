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


    @Mapping(target = "technicianId", source = "technicianUser.id")
    @Mapping(target = "technicianName", source = "technicianUser.fullName")

    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "vehicleLicensePlate", source = "vehicle.licensePlate")
    @Mapping(target = "vehicleModel", source = "vehicle.model.name")

    @Mapping(target = "servicePackageId", source = "servicePackage.id")
    @Mapping(target = "servicePackageName", source = "servicePackage.name")

    @Mapping(target = "serviceItems", source = "serviceItems")

    MaintenanceRecordResponse toMaintenanceRecordResponse(MaintenanceRecord maintenanceRecord);
}
