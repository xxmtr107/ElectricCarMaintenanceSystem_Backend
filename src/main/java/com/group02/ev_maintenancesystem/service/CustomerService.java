package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.CustomerRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse registerCustomer(CustomerRegistrationRequest request);
    CustomerResponse updateCustomer(Long customerId, CustomerUpdateRequest request);
    CustomerResponse getCustomerById(Long customerId);
    List<CustomerResponse> getAllCustomers();
    void deleteCustomer(Long customerId);
}
