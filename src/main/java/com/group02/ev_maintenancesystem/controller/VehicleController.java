package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.StatusUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.VehicleResponse;
import com.group02.ev_maintenancesystem.service.VehicleService;
import com.group02.ev_maintenancesystem.service.VehicleServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<VehicleResponse> createVehicle(@Valid @RequestBody VehicleCreationRequest request) {
        return ApiResponse.<VehicleResponse>builder()
                .message("Create vehicle succesfully")
                .result(vehicleService.createVehicle(request))
                .build();
    }

    //Lấy vehicles theo customerId
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF') or #customerId == authentication.principal.claims['userId']")
    public ApiResponse<List<VehicleResponse>> getVehicleCustomerId(@PathVariable Long customerId){
        return ApiResponse.<List<VehicleResponse>>builder()
                .message("Get vehicles succesfully")
                .result(vehicleService.getVehiclesByUserId(customerId))
                .build();
    }

    //Lấy vehicles theo vehicleId
    @GetMapping("/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF','TECHNICIAN') or @vehicleServiceImpl.isVehicleOwner(authentication, #vehicleId)")
    public ApiResponse<VehicleResponse> getVehicleByVehicleId(@PathVariable Long vehicleId){
        return ApiResponse.<VehicleResponse>builder()
                .message("Get vehicles succesfully")
                .result(vehicleService.getVehiclesByVehicleId(vehicleId))
                .build();
    }

    //Lấy tất cả vehicle
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<Page<VehicleResponse>> getAllVehicle(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ApiResponse.<Page<VehicleResponse>>builder()
                .message("Get all vehicles succesfully")
                .result(vehicleService.getAllVehicle(page, size))
                .build();
    }

    //Cập nhâp vehicles
    @PutMapping("/{vehicleId}")
    // [THAY ĐỔI] Bỏ @vehicleServiceImpl.isVehicleOwner -> Khách không được tự sửa Km
    // [THAY ĐỔI] Thêm 'TECHNICIAN' -> Chỉ nhân viên kỹ thuật/quản lý mới được cập nhật Km thực tế
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TECHNICIAN')")
    public ApiResponse<VehicleResponse> updateVehicle(@PathVariable Long vehicleId, @Valid @RequestBody VehicleUpdateRequest request){
        return ApiResponse.<VehicleResponse>builder()
                .message("Update vehicles successfully")
                .result(vehicleService.updateVehicle(vehicleId, request))
                .build();
    }

    //Xóa vehicles
    @DeleteMapping("/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF') or @vehicleServiceImpl.isVehicleOwner(authentication, #vehicleId)")
    public ApiResponse<VehicleResponse> deleteVehicle(@PathVariable Long vehicleId){
        return ApiResponse.<VehicleResponse>builder()
                .message("Delete vehicles succesfully")
                .result(vehicleService.deleteVehicle(vehicleId))
                .build();
    }
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF') or @vehicleServiceImpl.isVehicleOwner(authentication, #id)")
    public ApiResponse<VehicleResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {

        return ApiResponse.<VehicleResponse>builder()
                .message("Vehicle status updated")
                .result(vehicleService.updateStatus(id, request.getIsActive()))
                .build();
    }





}
