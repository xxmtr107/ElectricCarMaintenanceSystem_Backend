package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.MaintenanceRecordRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.MaintenanceRecordUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.MaintenanceRecordResponse;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.repository.MaintenanceRecordRepository;
import com.group02.ev_maintenancesystem.service.MaintenanceRecordService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ApiResponse<List<MaintenanceRecordResponse>> getAllMaintenanceRecords() {
        return ApiResponse.<List<MaintenanceRecordResponse>>builder().
                message("All maintenance record fetched successfully").
                result(maintenanceRecordService.getAll()).
                build();
    }

    @GetMapping("/{id}")
    public ApiResponse<MaintenanceRecordResponse>getByMaintenanceRecordId(@PathVariable long id){
        return ApiResponse.<MaintenanceRecordResponse>builder().
                message("Maintenance record fetched successfully").
                result(maintenanceRecordService.getByMaintenanceRecordId(id)).
                build();
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ApiResponse<List<MaintenanceRecordResponse>>getByVehicleId(@PathVariable long vehicleId){
        return ApiResponse.<List<MaintenanceRecordResponse>>builder().
                message("All maintenance record fetched successfully").
                result(maintenanceRecordService.findMaintenanceRecordsByVehicleId(vehicleId)).
                build();
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<MaintenanceRecordResponse>getByCustomerId(@PathVariable long customerId){
        return ApiResponse.<MaintenanceRecordResponse>builder().
                message("All maintenance records fetched successfully").
                result(maintenanceRecordService.findByCustomerId(customerId)).
                build();
    }

    @GetMapping("technician/{technicianId}")
    public ApiResponse<List<MaintenanceRecordResponse>>getByTechnicianId(@PathVariable long technicianId){
        return ApiResponse.<List<MaintenanceRecordResponse>>builder().
                message("All maintenance records fetched successfully").
                result(maintenanceRecordService.findByTechnicianUserId(technicianId)).
                build();
    }

    @GetMapping("/date-range")
    public ApiResponse<List<MaintenanceRecordResponse>>
    getByDateRange(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime startDate,
                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime endDate){
        return ApiResponse.<List<MaintenanceRecordResponse>>builder().
                message("All maintenance records fetched successfully with date range").
                result(maintenanceRecordService.findByAppointment_AppointmentDateBetween(startDate, endDate)).
                build();
    }

    @PostMapping
    public ApiResponse<MaintenanceRecordResponse>
    createMaintenanceRecord(Authentication authentication,
                            @RequestBody MaintenanceRecordRegistrationRequest maintenanceRecordRegistrationRequest){
        return ApiResponse.<MaintenanceRecordResponse>builder().
                message("Maintenance record created successfully").
                result(maintenanceRecordService.createMaintenanceRecord(authentication, maintenanceRecordRegistrationRequest)).
                build();
    }

    @PutMapping("/{id}")
    public ApiResponse<MaintenanceRecordResponse>
    updateMaintenanceRecord(@PathVariable long id,
                            @RequestBody MaintenanceRecordUpdateRequest maintenanceRecord,
                            Authentication authentication){
        return ApiResponse.<MaintenanceRecordResponse>builder().
                message("Maintenance record updated successfully").
                result(maintenanceRecordService.update(id,maintenanceRecord,authentication)).
                build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteMaintenanceRecord(@PathVariable long id){

            maintenanceRecordService.delete(id);
            return ApiResponse.<String>builder()
                    .message("Maintenance record deleted successfully")
                    .build();
    }
}
