package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.request.CustomerRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.CustomerResponse;
import com.group02.ev_maintenancesystem.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE, // Bỏ qua các fields không được mapping
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // field có giá trị null thì bỏ qua
public interface CustomerMapper {
    // Mapping CustomerRegistrationRequest sang User
    User toCustomer(CustomerRegistrationRequest request);

    // Update thông tin Customer từ CustomerUpdateRequest
    void updateCustomer(CustomerUpdateRequest request, @MappingTarget User customer); // @MappingTarget để chỉ định đối tượng đích cần cập nhật

    // Mapping User sang CustomerResponse
    @Mapping(target = "role", source = "role.name") // Chuyển đổi role.name sang role trong CustomerResponse
    CustomerResponse toCustomerResponse(User customer);
}
