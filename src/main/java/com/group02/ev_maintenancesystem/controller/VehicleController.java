package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.VehicleCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.VehicleResponse;
import com.group02.ev_maintenancesystem.repository.VehicleRepository;
import com.group02.ev_maintenancesystem.service.VehicleService;
import com.group02.ev_maintenancesystem.service.VehicleServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@Slf4j
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @Autowired
    VehicleServiceImpl vehicleServiceImpl;

    //Tạo mới 1 xe
    @PostMapping("/create")
    public ApiResponse<VehicleResponse> createVehicle(@Valid @RequestBody VehicleCreationRequest request){
        return ApiResponse.<VehicleResponse>builder()
                .message("Create vehicle succesfully")
                .result(vehicleService.createVehicle(request))
                .build();
    }

    //Lấy vehicles theo customerId
    @GetMapping("customer/{customerId}")
    public ApiResponse<List<VehicleResponse>> getVehicleyCustomerId(@PathVariable Long customerId){
        return ApiResponse.<List<VehicleResponse>>builder()
                .message("Get vehicles succesfully")
                .result(vehicleService.getVehiclesByUserId(customerId))
                .build();
    }

    //Lấy vehicles theo vehicleId
    @GetMapping("/{vehicleId}")
    public ApiResponse<VehicleResponse> getVehiclebyVehicleId(@PathVariable Long vehicleId){
        return ApiResponse.<VehicleResponse>builder()
                .message("Get vehicles succesfully")
                .result(vehicleService.getVehiclesByVehicleId(vehicleId))
                .build();
    }

    //Lấy tất cả vehicle
    @GetMapping
    public ApiResponse<List<VehicleResponse>> getAllVehicle(){
        return ApiResponse.<List<VehicleResponse>>builder()
                .message("Get all vehicles succesfully")
                .result(vehicleService.getAllVehicle())
                .build();
    }

    //Cập nhâp vehicles
    @PutMapping("/{vehicleId}")
    public ApiResponse<VehicleResponse> updateVehicle(@PathVariable Long vehicleId, @Valid @RequestBody VehicleUpdateRequest request){
        return ApiResponse.<VehicleResponse>builder()
                .message("Update vehicles successfully")
                .result(vehicleService.updateVehicle(vehicleId, request))
                .build();
    }

    @DeleteMapping("/{vehicleId}")
    public ApiResponse<VehicleResponse> deleteVehicle(@PathVariable Long vehicleId){
        return ApiResponse.<VehicleResponse>builder()
                .message("Delete vehicles succesfully")
                .result(vehicleService.deleteVehicle(vehicleId))
                .build();
    }





}
