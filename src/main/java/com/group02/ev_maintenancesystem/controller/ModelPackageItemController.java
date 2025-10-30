package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.ModelPackageItemRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.ModelPackageItemResponse;
import com.group02.ev_maintenancesystem.service.ModelPackageItemService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/model-package-items") // Giữ nguyên base path
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelPackageItemController {

    ModelPackageItemService modelPackageItemService;

    @PostMapping
    public ApiResponse<ModelPackageItemResponse> createModelPackageItem(
            @Valid @RequestBody ModelPackageItemRequest request) {
        return ApiResponse.<ModelPackageItemResponse>builder()
                .message("Model package item created successfully") // Thêm message
                .result(modelPackageItemService.createModelPackageItem(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ModelPackageItemResponse> getModelPackageItemById(@PathVariable Long id) {
        return ApiResponse.<ModelPackageItemResponse>builder()
                .message("Model package item fetched successfully") // Thêm message
                .result(modelPackageItemService.getModelPackageItemById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ModelPackageItemResponse>> getAllModelPackageItems() {
        return ApiResponse.<List<ModelPackageItemResponse>>builder()
                .message("All model package items fetched successfully") // Thêm message
                .result(modelPackageItemService.getAllModelPackageItems())
                .build();
    }

    @GetMapping("/vehicle-model/{vehicleModelId}")
    public ApiResponse<List<ModelPackageItemResponse>> getByVehicleModelId(
            @PathVariable Long vehicleModelId) {
        return ApiResponse.<List<ModelPackageItemResponse>>builder()
                .message("Model package items fetched by model successfully") // Thêm message
                .result(modelPackageItemService.getByVehicleModelId(vehicleModelId))
                .build();
    }

    // --- Endpoint này không còn phù hợp, có thể bỏ ---
    // @GetMapping("/service-package/{servicePackageId}")
    // public ApiResponse<List<ModelPackageItemResponse>> getByServicePackageId(...)

    // --- Endpoint này đổi thành theo milestoneKm ---
    // @GetMapping("/vehicle-model/{vehicleModelId}/package/{servicePackageId}")
    // public ApiResponse<List<ModelPackageItemResponse>> getByVehicleModelAndPackage(...)
    @GetMapping("/vehicle-model/{vehicleModelId}/milestone/{milestoneKm}")
    public ApiResponse<List<ModelPackageItemResponse>> getByVehicleModelAndMilestone(
            @PathVariable Long vehicleModelId,
            @PathVariable Integer milestoneKm) {
        return ApiResponse.<List<ModelPackageItemResponse>>builder()
                .message("Model package items fetched by model and milestone successfully") // Thêm message
                .result(modelPackageItemService.getByVehicleModelAndMilestoneKm(vehicleModelId, milestoneKm))
                .build();
    }

    // --- Endpoint này có thể không cần nữa ---
    // @GetMapping("/vehicle-model/{vehicleModelId}/individual-services")
    // public ApiResponse<List<ModelPackageItemResponse>> getIndividualServicesByModel(...)

    @PutMapping("/{id}")
    public ApiResponse<ModelPackageItemResponse> updateModelPackageItem(
            @PathVariable Long id,
            @Valid @RequestBody ModelPackageItemRequest request) {
        return ApiResponse.<ModelPackageItemResponse>builder()
                .message("Model package item updated successfully") // Thêm message
                .result(modelPackageItemService.updateModelPackageItem(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteModelPackageItem(@PathVariable Long id) {
        modelPackageItemService.deleteModelPackageItem(id);
        return ApiResponse.<Void>builder()
                .message("Model package item deleted successfully")
                .build();
    }

    // --- Endpoint này đổi thành tính tổng giá theo milestone ---
    // @GetMapping("/total/model/{vehicleModelId}/package/{servicePackageId}")
    // public ApiResponse<BigDecimal> getPackageTotalPrice(...)
    @GetMapping("/total/model/{vehicleModelId}/milestone/{milestoneKm}")
    public ApiResponse<BigDecimal> getMilestoneTotalPrice(
            @PathVariable Long vehicleModelId,
            @PathVariable Integer milestoneKm) {
        BigDecimal total = modelPackageItemService.getMilestoneTotalPrice(vehicleModelId, milestoneKm);
        return ApiResponse.<BigDecimal>builder()
                .message("Total price for milestone calculated successfully") // Thêm message
                .result(total)
                .build();
    }
}