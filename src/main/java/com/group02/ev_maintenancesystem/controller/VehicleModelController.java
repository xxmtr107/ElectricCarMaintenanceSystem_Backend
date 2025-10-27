package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.VehicleModelRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleModelUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.*;
import com.group02.ev_maintenancesystem.service.VehicleModelService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize; // Giữ lại nếu cần
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicleModel") // Giữ nguyên base path
public class VehicleModelController {
    @Autowired // Giữ lại Autowired nếu bạn không dùng Constructor Injection
    VehicleModelService vehicleModelService;

    // Request giờ chỉ cần name và modelYear
    @PostMapping("/create")
    // @PreAuthorize("hasRole('ADMIN')") // Thêm quyền nếu cần
    public ApiResponse<VehicleModelResponse> createVehicleModel(@Valid @RequestBody VehicleModelRequest request){
        return ApiResponse. <VehicleModelResponse>builder()
                .message("Create vehicle model successfully")
                .result(vehicleModelService.createVehicleModel(request))
                .build();
    }

    @GetMapping("/{vehicleModelId}")
    // @PreAuthorize("permitAll()") // Hoặc quyền phù hợp
    public ApiResponse<VehicleModelResponse> getVehicleModelById(@PathVariable Long vehicleModelId){
        return ApiResponse.<VehicleModelResponse>builder()
                .message("Get vehicle model succesfully") // Sửa lỗi chính tả
                .result(vehicleModelService.getVehicleModelById(vehicleModelId))
                .build();
    }

    @GetMapping
    // @PreAuthorize("permitAll()") // Hoặc quyền phù hợp
    public ApiResponse<List<VehicleModelResponse>> getAllVehicleModel(){
        return ApiResponse.<List<VehicleModelResponse>>builder()
                .message("Get all vehicle models successfully") // Sửa message
                .result(vehicleModelService.getAllVehicleModel())
                .build();
    }

    @GetMapping("/search")
    // @PreAuthorize("permitAll()") // Hoặc quyền phù hợp
    public ApiResponse<Page<VehicleModelResponse>> getVehicleModelByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ApiResponse.<Page<VehicleModelResponse>>builder()
                .message("Search vehicle models successfully") // Sửa message
                .result(vehicleModelService.searchVehicleModelByName(keyword, page, size))
                .build();
    }

    // Request giờ chỉ có thể cập nhật name và modelYear
    @PutMapping("/{vehicleModelId}")
    // @PreAuthorize("hasRole('ADMIN')") // Thêm quyền nếu cần
    public ApiResponse<VehicleModelResponse> updateVehicleModel(
            @PathVariable Long vehicleModelId,
            @RequestBody @Valid VehicleModelUpdateRequest request){ // Request đã được cập nhật
        return ApiResponse.<VehicleModelResponse>builder()
                .message("Update vehicle model successfully") // Sửa message
                .result(vehicleModelService.updateVehicleModel(vehicleModelId, request)) // Đổi tên hàm trong service
                .build();
    }

    @DeleteMapping("/{vehicleModelId}")
    // @PreAuthorize("hasRole('ADMIN')") // Thêm quyền nếu cần
    public ApiResponse<String> deleteVehicleModelById(@PathVariable Long vehicleModelId){ // Trả về String message
        vehicleModelService.deleteVehicleModelById(vehicleModelId); // Hàm service không cần trả về response
        return ApiResponse.<String>builder() // Giữ nguyên String response
                .message("Delete vehicle model successfully")
                // .result(...) // Không cần result
                .build();
    }
}