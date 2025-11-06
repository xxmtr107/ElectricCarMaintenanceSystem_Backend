package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.PasswordUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.UserRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;
import com.group02.ev_maintenancesystem.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    ApiResponse<UserResponse> register(@RequestBody @Valid UserRegistrationRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Customer registered successfully")
                .result(customerService.registerCustomer(request))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    ApiResponse<List<UserResponse>> findAll(){
        return ApiResponse.<List<UserResponse>>builder()
                .message("Customers fetched successfully")
                .result(customerService.getAllCustomers())
                .build();
    }

    @PutMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    ApiResponse<UserResponse> updateCustomer(@PathVariable Long customerId, @RequestBody @Valid UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Customer updated successfully")
                .result(customerService.updateCustomer(customerId,request))
                .build();
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    ApiResponse<UserResponse> getCustomer(@PathVariable Long customerId){
        return ApiResponse.<UserResponse>builder()
                .message("Customer fetched successfully")
                .result(customerService.getCustomerById(customerId))
                .build();
    }
    @GetMapping("/my-info")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(customerService.getMyInfo())
                .build();
    }
    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    ApiResponse<String> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ApiResponse.<String>builder()
                .message("Customer deleted successfully")
                .build();
    }

    @PutMapping("/change-password") // Hoặc "/change-password"
    @PreAuthorize("isAuthenticated()") // Bất kỳ ai đã đăng nhập đều có thể đổi pass của mình
    public ApiResponse<Void> changeMyPassword(@RequestBody @Valid PasswordUpdateRequest request) {
        customerService.changeMyPassword(request); // Gọi service mới
        return ApiResponse.<Void>builder()
                .message("Password updated successfully")
                .build(); // Không cần trả về user info, chỉ cần thông báo thành công
    }

    @PutMapping("/my-info")
    @PreAuthorize("hasRole('CUSTOMER')") // Chỉ customer mới tự update info
    public ApiResponse<UserResponse> updateMyInfo(@RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("Customer info updated successfully")
                .result(customerService.updateMyInfo(request)) // Gọi service mới
                .build();
    }

}
