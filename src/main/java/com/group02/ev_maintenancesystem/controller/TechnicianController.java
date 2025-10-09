package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.TechnicianRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.TechnicianUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.TechnicianResponse;
import com.group02.ev_maintenancesystem.service.TechnicianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    ApiResponse<TechnicianResponse> register(@RequestBody @Valid TechnicianRegistrationRequest request){
        return ApiResponse.<TechnicianResponse>builder()
                .message("Technician registered successfully")
                .result(technicianService.registerTechnician(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<TechnicianResponse>> findAll(){
        return ApiResponse.<List<TechnicianResponse>>builder()
                .message("Technicians fetched successfully")
                .result(technicianService.getAllTechnicians())
                .build();
    }

    @PutMapping("/{technicianId}")
    ApiResponse<TechnicianResponse> updateTechnician(@PathVariable Long technicianId,@RequestBody @Valid TechnicianUpdateRequest request){
        return ApiResponse.<TechnicianResponse>builder()
                .message("Technician updated successfully")
                .result(technicianService.updateTechnician(technicianId,request))
                .build();
    }

    @GetMapping("/{technicianId}")
    ApiResponse<TechnicianResponse> getTechnician(@PathVariable Long technicianId){
        return ApiResponse.<TechnicianResponse>builder()
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
