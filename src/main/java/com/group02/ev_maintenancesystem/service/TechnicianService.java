package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.UserRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;

import java.util.List;

public interface TechnicianService {
    UserResponse registerTechnician(UserRegistrationRequest request);
    UserResponse updateTechnician(Long technicianId, UserUpdateRequest request);
    UserResponse getTechnicianById(Long technicianId);
    List<UserResponse> getAllTechnicians();
    void deleteTechnician(Long technicianId);
    List<UserResponse> getTechniciansByCenter(Long centerId);
}
