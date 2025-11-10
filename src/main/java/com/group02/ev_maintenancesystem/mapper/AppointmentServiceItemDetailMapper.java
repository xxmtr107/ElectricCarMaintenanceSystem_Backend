package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.response.AppointmentServiceItemDetailResponse;
import com.group02.ev_maintenancesystem.entity.AppointmentServiceItemDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppointmentServiceItemDetailMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "serviceItemId", source = "serviceItem.id")
    @Mapping(target = "serviceItemName", source = "serviceItem.name")
    @Mapping(target = "actionType", source = "actionType")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "customerApproved", source = "customerApproved")
    @Mapping(target = "technicianNotes", source = "technicianNotes")
    AppointmentServiceItemDetailResponse toResponse(AppointmentServiceItemDetail detail);
}