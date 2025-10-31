package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.response.PartUsageResponse;
import com.group02.ev_maintenancesystem.entity.PartUsage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PartUsageMapper {

    @Mapping(target = "sparePartId", source = "sparePart.id")
    @Mapping(target = "sparePartName", source = "sparePart.name")
    @Mapping(target = "sparePartNumber", source = "sparePart.partNumber")
    @Mapping(target = "maintenanceRecordId", source = "maintenanceRecord.id")
    PartUsageResponse toPartUsageResponse(PartUsage partUsage);
}