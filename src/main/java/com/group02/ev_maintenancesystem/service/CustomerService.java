package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.PasswordUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.UserRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;

import java.util.List;

public interface CustomerService {
    UserResponse registerCustomer(UserRegistrationRequest request);
    UserResponse updateCustomer(Long customerId, UserUpdateRequest request);
    UserResponse getCustomerById(Long customerId);
    List<UserResponse> getAllCustomers();
    void deleteCustomer(Long customerId);
    UserResponse getMyInfo();
    void changeMyPassword(PasswordUpdateRequest request);
    UserResponse updateMyInfo(UserUpdateRequest request);
    UserResponse updateUserStatus(Long userId, Boolean isActive);
}
