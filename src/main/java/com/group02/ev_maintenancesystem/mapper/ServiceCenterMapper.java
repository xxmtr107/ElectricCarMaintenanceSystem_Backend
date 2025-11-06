package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.request.ServiceCenterRequest;
import com.group02.ev_maintenancesystem.dto.response.ServiceCenterResponse;
import com.group02.ev_maintenancesystem.entity.ServiceCenter;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ServiceCenterMapper {

    ServiceCenter toServiceCenter(ServiceCenterRequest request);

    ServiceCenterResponse toServiceCenterResponse(ServiceCenter center);

    void updateServiceCenter(ServiceCenterRequest request, @MappingTarget ServiceCenter center);
}