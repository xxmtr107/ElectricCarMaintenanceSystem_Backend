package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.request.TechnicianRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.TechnicianUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.TechnicianResponse;
import com.group02.ev_maintenancesystem.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TechnicianMapper {
    User toTechnician(TechnicianRegistrationRequest request);

    void updateTechnician(TechnicianUpdateRequest request, @MappingTarget User Technician);

    @Mapping(target = "role", source = "role.name")
    TechnicianResponse toTechnicianResponse(User Technician);
}
