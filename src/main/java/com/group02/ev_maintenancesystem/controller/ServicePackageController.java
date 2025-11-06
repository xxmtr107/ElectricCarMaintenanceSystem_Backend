package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.ServicePackageCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.ServicePackageUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.ServicePackageResponse;
import com.group02.ev_maintenancesystem.entity.ServicePackage;
import com.group02.ev_maintenancesystem.service.ServicePackageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/servicePackages")
public class ServicePackageController {
    @Autowired
    ServicePackageService servicePackageService;

    @PostMapping("/create")
    public ApiResponse<ServicePackageResponse> createServicePackage(@Valid @RequestBody ServicePackageCreationRequest request){
        return ApiResponse.<ServicePackageResponse>builder()
                .message("Create service package successfully")
                .result(servicePackageService.createServicePackage(request))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<ServicePackageResponse>> getAllServicePackage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ApiResponse.<Page<ServicePackageResponse>>builder()
                .message("Get all service package successfully")
                .result(servicePackageService.getAllServicePackage(page, size))
                .build();
    }

    @GetMapping("/{servicePackageId}")
    public ApiResponse<ServicePackageResponse> getServicePackageById(@PathVariable Long servicePackageId){
        return ApiResponse.<ServicePackageResponse>builder()
                .message("Get service package successfully")
                .result(servicePackageService.getServicePackageById(servicePackageId))
                .build();
    }

    @PutMapping("/{servicePackageId}")
    public ApiResponse<ServicePackageResponse> updateServicePackage(@PathVariable Long servicePackageId, @RequestBody ServicePackageUpdateRequest request){
        return ApiResponse.<ServicePackageResponse>builder()
                .message("Update service package successfully")
                .result(servicePackageService.updateServicePackageById(servicePackageId, request))
                .build();
    }

    @DeleteMapping("/{servicePackageId}")
    public ApiResponse<ServicePackageResponse> deleteServicePackage(@PathVariable Long servicePackageId){
        return ApiResponse.<ServicePackageResponse>builder()
                .message("Delete service package successfully")
                .result(servicePackageService.deleteServicePackageById(servicePackageId))
                .build();
    }
}
