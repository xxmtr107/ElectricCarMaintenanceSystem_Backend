package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.PartUsageRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.MaintenanceRecordResponse;
import com.group02.ev_maintenancesystem.dto.response.PartUsageDetailResponse;
import com.group02.ev_maintenancesystem.dto.response.PartUsageResponse;
import com.group02.ev_maintenancesystem.service.MaintenanceRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/maintenance-records")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MaintenanceRecordController {

    MaintenanceRecordService maintenanceRecordService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TECHNICIAN')") // Yêu cầu 2
    public ApiResponse<List<MaintenanceRecordResponse>> getAllMaintenanceRecords(Authentication authentication) {
        return ApiResponse.<List<MaintenanceRecordResponse>>builder().
                message("All maintenance record fetched successfully").
                result(maintenanceRecordService.getAll(authentication)).
                build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<MaintenanceRecordResponse>getByMaintenanceRecordId(@PathVariable long id){
        return ApiResponse.<MaintenanceRecordResponse>builder().
                message("Maintenance record fetched successfully").
                result(maintenanceRecordService.getByMaintenanceRecordId(id)).
                build();
    }

    @GetMapping("/vehicle/{vehicleId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<MaintenanceRecordResponse>>getByVehicleId(@PathVariable long vehicleId){
        return ApiResponse.<List<MaintenanceRecordResponse>>builder().
                message("All maintenance record fetched successfully").
                result(maintenanceRecordService.findMaintenanceRecordsByVehicleId(vehicleId)).
                build();
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF') or #customerId == authentication.principal.claims['userId']")
    public ApiResponse<List<MaintenanceRecordResponse>>getByCustomerId(@PathVariable long customerId){
        return ApiResponse.<List<MaintenanceRecordResponse>>builder().
                message("All maintenance records fetched successfully").
                result(maintenanceRecordService.findByCustomerId(customerId)).
                build();
    }

    @GetMapping("technician/{technicianId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF') or (hasRole('TECHNICIAN') and #technicianId == authentication.principal.claims['userId'])") // Yêu cầu 1
    public ApiResponse<List<MaintenanceRecordResponse>>getByTechnicianId(@PathVariable long technicianId){
        return ApiResponse.<List<MaintenanceRecordResponse>>builder().
                message("All maintenance records fetched successfully").
                result(maintenanceRecordService.findByTechnicianUserId(technicianId)).
                build();
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TECHNICIAN')") // Yêu cầu 2
    public ApiResponse<List<MaintenanceRecordResponse>>
    getByDateRange(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime startDate,
                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime endDate,
                   Authentication authentication){
        return ApiResponse.<List<MaintenanceRecordResponse>>builder().
                message("All maintenance records fetched successfully with date range").
                result(maintenanceRecordService.findByAppointment_AppointmentDateBetween(startDate, endDate, authentication)).
                build();
    }

    /**
     * Endpoint cho Technician thêm phụ tùng PHÁT SINH (ngoài gói)
     */
    @PostMapping("/{recordId}/parts")
    @PreAuthorize("hasRole('TECHNICIAN')") // Yêu cầu 1
    public ApiResponse<PartUsageResponse> addPartToRecord(
            @PathVariable Long recordId,
            @Valid @RequestBody PartUsageRequest request,
            Authentication authentication) {
        return ApiResponse.<PartUsageResponse>builder()
                .message("Part added to maintenance record successfully")
                .result(maintenanceRecordService.addPartToRecord(recordId, request, authentication))
                .build();
    }

    @GetMapping("/{id}/all-parts")
    @PreAuthorize("isAuthenticated()") // Bất kỳ ai có quyền xem record đều có thể xem
    public ApiResponse<List<PartUsageDetailResponse>> getAllPartsForRecord(
            @PathVariable long id, Authentication authentication) {
        return ApiResponse.<List<PartUsageDetailResponse>>builder()
                .message("All parts used for record fetched successfully")
                .result(maintenanceRecordService.getAllPartUsagesForRecord(id, authentication))
                .build();
    }
}