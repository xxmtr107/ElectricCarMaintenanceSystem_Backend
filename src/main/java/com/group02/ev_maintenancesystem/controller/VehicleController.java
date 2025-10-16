package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.VehicleCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.VehicleResponse;
import com.group02.ev_maintenancesystem.service.VehicleService;
import com.group02.ev_maintenancesystem.service.VehicleServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin("*")
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
    @GetMapping("/customer/{customerId}")
    public ApiResponse<List<VehicleResponse>> getVehicleCustomerId(@PathVariable Long customerId){
        return ApiResponse.<List<VehicleResponse>>builder()
                .message("Get vehicles succesfully")
                .result(vehicleService.getVehiclesByUserId(customerId))
                .build();
    }

    //Lấy vehicles theo vehicleId
    @GetMapping("/{vehicleId}")
    public ApiResponse<VehicleResponse> getVehicleByVehicleId(@PathVariable Long vehicleId){
        return ApiResponse.<VehicleResponse>builder()
                .message("Get vehicles succesfully")
                .result(vehicleService.getVehiclesByVehicleId(vehicleId))
                .build();
    }

    //Lấy tất cả vehicle
    @GetMapping
    public ApiResponse<Page<VehicleResponse>> getAllVehicle(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ApiResponse.<Page<VehicleResponse>>builder()
                .message("Get all vehicles succesfully")
                .result(vehicleService.getAllVehicle(page, size))
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

    //Xóa vehicles
    @DeleteMapping("/{vehicleId}")
    public ApiResponse<VehicleResponse> deleteVehicle(@PathVariable Long vehicleId){
        return ApiResponse.<VehicleResponse>builder()
                .message("Delete vehicles succesfully")
                .result(vehicleService.deleteVehicle(vehicleId))
                .build();
    }






}
