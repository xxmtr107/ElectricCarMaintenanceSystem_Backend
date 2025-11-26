package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.ServiceCenterRequest;
import com.group02.ev_maintenancesystem.dto.request.StatusUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.ServiceCenterResponse;
import com.group02.ev_maintenancesystem.service.ServiceCenterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service-centers")
@RequiredArgsConstructor
public class ServiceCenterController {

    private final ServiceCenterService serviceCenterService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ServiceCenterResponse> createServiceCenter(@Valid @RequestBody ServiceCenterRequest request) {
        return ApiResponse.<ServiceCenterResponse>builder()
                .message("Service center created successfully")
                .result(serviceCenterService.createServiceCenter(request))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<ServiceCenterResponse>> getAllServiceCenters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<Page<ServiceCenterResponse>>builder()
                .message("Service centers fetched successfully")
                .result(serviceCenterService.getAllServiceCenters(page, size))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<ServiceCenterResponse>> searchServiceCenters(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<Page<ServiceCenterResponse>>builder()
                .message("Service centers searched successfully")
                .result(serviceCenterService.searchServiceCenters(keyword, page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ServiceCenterResponse> getServiceCenterById(@PathVariable Long id) {
        return ApiResponse.<ServiceCenterResponse>builder()
                .message("Service center fetched successfully")
                .result(serviceCenterService.getServiceCenterById(id))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ServiceCenterResponse> updateServiceCenter(
            @PathVariable Long id,
            @Valid @RequestBody ServiceCenterRequest request) {
        return ApiResponse.<ServiceCenterResponse>builder()
                .message("Service center updated successfully")
                .result(serviceCenterService.updateServiceCenter(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteServiceCenter(@PathVariable Long id) {
        serviceCenterService.deleteServiceCenter(id);
        return ApiResponse.<Void>builder()
                .message("Service center deleted successfully")
                .build();
    }
    // Trong ServiceCenterController.java
    @PatchMapping("/{id}/status")
    public ApiResponse<ServiceCenterResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {

        return ApiResponse.<ServiceCenterResponse>builder()
                .message("Service Center status updated")
                .result(serviceCenterService.updateStatus(id, request.getIsActive()))
                .build();
    }
}