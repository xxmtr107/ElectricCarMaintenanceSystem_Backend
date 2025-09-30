package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
import com.group02.ev_maintenancesystem.dto.request.CustomerRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.CustomerResponse;
import com.group02.ev_maintenancesystem.entity.Role;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.CustomerMapper;
import com.group02.ev_maintenancesystem.repository.CustomerRepository;
import com.group02.ev_maintenancesystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomerServiceImpl implements CustomerService {
    CustomerRepository customerRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    CustomerMapper customerMapper;

    @Override
    public CustomerResponse registerCustomer(CustomerRegistrationRequest request) {
        User customer = customerMapper.toCustomer(request);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName(PredefinedRole.CUSTOMER)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        customer.setRole(role);
        try {
            customer = customerRepository.save(customer);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message.contains("UK_username")) {
                throw new AppException(ErrorCode.USERNAME_EXISTED);
            }
            else if(message.contains("UK_email")) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
        }
        return customerMapper.toCustomerResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(Long customerId, CustomerUpdateRequest request) {
        User customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        customerMapper.updateCustomer(request, customer);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId) {
        return customerMapper.toCustomerResponse(
                customerRepository.findById(customerId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND))
        );
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toCustomerResponse)
                .toList();
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }
}
