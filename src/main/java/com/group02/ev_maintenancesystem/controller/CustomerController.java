package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.CustomerRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.CustomerUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.CustomerResponse;
import com.group02.ev_maintenancesystem.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomerController {
    CustomerService customerService;

    @PostMapping("/register")
    ApiResponse<CustomerResponse> register(@RequestBody @Valid CustomerRegistrationRequest request){
        return ApiResponse.<CustomerResponse>builder()
                .message("Customer registered successfully")
                .result(customerService.registerCustomer(request))
                .build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<CustomerResponse>> findAll(){
        return ApiResponse.<List<CustomerResponse>>builder()
                .message("Customers fetched successfully")
                .result(customerService.getAllCustomers())
                .build();
    }

    @PutMapping("/update/{customerId}")
    ApiResponse<CustomerResponse> updateCustomer(@PathVariable Long customerId,@RequestBody @Valid CustomerUpdateRequest request){
        return ApiResponse.<CustomerResponse>builder()
                .message("Customer updated successfully")
                .result(customerService.updateCustomer(customerId,request))
                .build();
    }

    @GetMapping("/getById/{customerId}")
    ApiResponse<CustomerResponse> getCustomer(@PathVariable Long customerId){
        return ApiResponse.<CustomerResponse>builder()
                .message("Customer fetched successfully")
                .result(customerService.getCustomerById(customerId))
                .build();
    }

    @DeleteMapping("/delete/{customerId}")
    ApiResponse<String> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ApiResponse.<String>builder()
                .message("Customer deleted successfully")
                .build();
    }



}
