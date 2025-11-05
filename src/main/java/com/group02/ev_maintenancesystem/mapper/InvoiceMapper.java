package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.response.InvoiceResponse;
import com.group02.ev_maintenancesystem.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MaintenanceRecordMapper.class}) // Cho phép map object MaintenanceRecord lồng nhau
public interface InvoiceMapper {

    @Mapping(target = "maintenanceRecordId", source = "maintenanceRecord.id")
    @Mapping(target = "serviceCenterId", source = "serviceCenter.id")
    @Mapping(target = "serviceCenterName", source = "serviceCenter.name")
    @Mapping(target = "maintenanceRecord", source = "maintenanceRecord") // Map object lồng nhau
    InvoiceResponse toInvoiceResponse(Invoice invoice);
}