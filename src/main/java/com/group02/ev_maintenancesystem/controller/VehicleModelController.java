package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.VehicleModelRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleModelUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.*;
import com.group02.ev_maintenancesystem.service.VehicleModelService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicleModel")
public class VehicleModelController {
    @Autowired
    VehicleModelService vehicleModelService;


    //Tạo 1 model xe mới
    @PostMapping("/create")
    public ApiResponse<VehicleModelResponse> createVehicleModel(@Valid @RequestBody VehicleModelRequest request){
        return ApiResponse. <VehicleModelResponse>builder()
                .message("Create vehicle model successfully")
                .result(vehicleModelService.createVehicleModel(request))
                .build();
    }

    //Lấy thông tin của model xe bằng id của model
    @GetMapping("/{vehicleModelId}")
    public ApiResponse<VehicleModelResponse> getVehicleModelById(@PathVariable Long vehicleModelId){
        return ApiResponse.<VehicleModelResponse>builder()
                .message("Get vehicle model succesfully")
                .result(vehicleModelService.getVehicleModelById(vehicleModelId))
                .build();
    }

    //Lấy tất cả model
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<List<VehicleModelResponse>> getAllVehicleModel(){
        return ApiResponse.<List<VehicleModelResponse>>builder()
                .message("Get vehicle model successfully")
                .result(vehicleModelService.getAllVehicleModel())
                .build();
    }

    //Search model bằng keyword là name
    @GetMapping("/search")
    public ApiResponse<Page<VehicleModelResponse>> getVehicleModelByKeyword(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ApiResponse.<Page<VehicleModelResponse>>builder()
                .message("Get vehicle model successfully")
                .result(vehicleModelService.searchVehicleModelByName(keyword, page, size))
                .build();
    }

    //Update model bằng id
    @PutMapping("/{vehicleModelId}")
    public ApiResponse<VehicleModelResponse> updateVehicleModel(@PathVariable Long vehicleModelId, @RequestBody @Valid VehicleModelUpdateRequest request){
        return ApiResponse.<VehicleModelResponse>builder()
                .message("Update vehicle successfully")
                .result(vehicleModelService.updateVehicleMode(vehicleModelId, request))
                .build();
    }

    //Delete bằng modelId
    @DeleteMapping("/{vehicleModelId}")
    public ApiResponse<VehicleModelResponse> deleteVehicleModelById(@PathVariable Long vehicleModelId){
        return ApiResponse.<VehicleModelResponse>builder()
                .message("Delete vehicle model successfully")
                .result(vehicleModelService.deleteVehicleModelById(vehicleModelId))
                .build();

    }

}
