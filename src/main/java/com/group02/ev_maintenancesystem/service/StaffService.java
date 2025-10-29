package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.UserRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;

import java.util.List;

public interface StaffService {
    UserResponse registerStaff(UserRegistrationRequest request);
    UserResponse updateStaff(Long staffId, UserUpdateRequest request);
    UserResponse getStaffById(Long staffId);
    List<UserResponse> getAllStaffs();
    void deleteStaff(Long staffId);
}
