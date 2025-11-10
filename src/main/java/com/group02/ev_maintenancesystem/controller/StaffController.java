package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.UserRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;
import com.group02.ev_maintenancesystem.service.StaffService;
import com.group02.ev_maintenancesystem.service.TechnicianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staffs")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class StaffController {
    StaffService staffService;

    @PostMapping("/register")
    ApiResponse<UserResponse> register(@RequestBody @Valid UserRegistrationRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Staff registered successfully")
                .result(staffService.registerStaff(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> findAll(){
        return ApiResponse.<List<UserResponse>>builder()
                .message("Staff fetched successfully")
                .result(staffService.getAllStaffs())
                .build();
    }

    @PutMapping("/{staffId}")
    ApiResponse<UserResponse> updateStaff(@PathVariable Long staffId,@RequestBody @Valid UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Staff updated successfully")
                .result(staffService.updateStaff(staffId,request))
                .build();
    }

    @GetMapping("/{staffId}")
    ApiResponse<UserResponse> getStaff(@PathVariable Long staffId){
        return ApiResponse.<UserResponse>builder()
                .message("Staff fetched successfully")
                .result(staffService.getStaffById(staffId))
                .build();
    }

    @GetMapping("/center/{centerId}")
    ApiResponse<List<UserResponse>> getStaffByCenter(@PathVariable Long centerId) {
        return ApiResponse.<List<UserResponse>>builder()
                .message("Staff fetched by center successfully")
                .result(staffService.getStaffByCenter(centerId))
                .build();
    }

    @DeleteMapping("/{staffId}")
    ApiResponse<String> deleteStaff(@PathVariable Long staffId) {
        staffService.deleteStaff(staffId);
        return ApiResponse.<String>builder()
                .message("Staff deleted successfully")
                .build();
    }



}
