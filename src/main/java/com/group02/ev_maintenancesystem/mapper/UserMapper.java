package com.group02.ev_maintenancesystem.mapper;

import com.group02.ev_maintenancesystem.dto.request.UserRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;
import com.group02.ev_maintenancesystem.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE, // Bỏ qua các fields không được mapping
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // field có giá trị null thì bỏ qua
public interface UserMapper {
    // Mapping UserRegistrationRequest sang User
    User toUser(UserRegistrationRequest request);

    // Update thông tin Customer từ UserUpdateRequest
    void updateUser(UserUpdateRequest request, @MappingTarget User user); // @MappingTarget để chỉ định đối tượng đích cần cập nhật

    // Mapping User sang UserResponse
    @Mapping(target = "role", source = "role.name") // Chuyển đổi role.name sang role trong UserResponse
    @Mapping(target = "serviceCenterId", source = "serviceCenter.id")
    @Mapping(target = "serviceCenterName", source = "serviceCenter.name")
    UserResponse toUserResponse(User user);
}
