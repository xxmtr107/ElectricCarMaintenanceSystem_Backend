package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.UserRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;
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
    ApiResponse<UserResponse> register(@RequestBody @Valid UserRegistrationRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Customer registered successfully")
                .result(customerService.registerCustomer(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> findAll(){
        return ApiResponse.<List<UserResponse>>builder()
                .message("Customers fetched successfully")
                .result(customerService.getAllCustomers())
                .build();
    }

    @PutMapping("/{customerId}")
    ApiResponse<UserResponse> updateCustomer(@PathVariable Long customerId, @RequestBody @Valid UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Customer updated successfully")
                .result(customerService.updateCustomer(customerId,request))
                .build();
    }

    @GetMapping("/{customerId}")
    ApiResponse<UserResponse> getCustomer(@PathVariable Long customerId){
        return ApiResponse.<UserResponse>builder()
                .message("Customer fetched successfully")
                .result(customerService.getCustomerById(customerId))
                .build();
    }
    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(customerService.getMyInfo())
                .build();
    }
    @DeleteMapping("/{customerId}")
    ApiResponse<String> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ApiResponse.<String>builder()
                .message("Customer deleted successfully")
                .build();
    }



}
