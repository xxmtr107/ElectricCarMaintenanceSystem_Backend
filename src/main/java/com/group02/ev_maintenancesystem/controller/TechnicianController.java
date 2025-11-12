package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.UserRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;
import com.group02.ev_maintenancesystem.service.TechnicianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/technicians")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianController {
    TechnicianService technicianService;

    @PostMapping("/register")
    ApiResponse<UserResponse> register(@RequestBody @Valid UserRegistrationRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Technician registered successfully")
                .result(technicianService.registerTechnician(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> findAll(Authentication authentication){
        return ApiResponse.<List<UserResponse>>builder()
                .message("Technicians fetched successfully")
                .result(technicianService.getAllTechnicians(authentication))
                .build();
    }

    @PutMapping("/{technicianId}")
    ApiResponse<UserResponse> updateTechnician(@PathVariable Long technicianId,@RequestBody @Valid UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Technician updated successfully")
                .result(technicianService.updateTechnician(technicianId,request))
                .build();
    }

    @GetMapping("/{technicianId}")
    ApiResponse<UserResponse> getTechnician(@PathVariable Long technicianId){
        return ApiResponse.<UserResponse>builder()
                .message("Technician fetched successfully")
                .result(technicianService.getTechnicianById(technicianId))
                .build();
    }

    @DeleteMapping("/{technicianId}")
    ApiResponse<String> deleteTechnician(@PathVariable Long technicianId) {
        technicianService.deleteTechnician(technicianId);
        return ApiResponse.<String>builder()
                .message("Technician deleted successfully")
                .build();
    }




}
