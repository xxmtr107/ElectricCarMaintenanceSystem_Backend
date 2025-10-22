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
@RequestMapping("/model-package-items")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelPackageItemController {

    ModelPackageItemService modelPackageItemService;

    @PostMapping
    public ApiResponse<ModelPackageItemResponse> createModelPackageItem(
            @Valid @RequestBody ModelPackageItemRequest request) {
        return ApiResponse.<ModelPackageItemResponse>builder()
                .result(modelPackageItemService.createModelPackageItem(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ModelPackageItemResponse> getModelPackageItemById(@PathVariable Long id) {
        return ApiResponse.<ModelPackageItemResponse>builder()
                .result(modelPackageItemService.getModelPackageItemById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ModelPackageItemResponse>> getAllModelPackageItems() {
        return ApiResponse.<List<ModelPackageItemResponse>>builder()
                .result(modelPackageItemService.getAllModelPackageItems())
                .build();
    }

    @GetMapping("/vehicle-model/{vehicleModelId}")
    public ApiResponse<List<ModelPackageItemResponse>> getByVehicleModelId(
            @PathVariable Long vehicleModelId) {
        return ApiResponse.<List<ModelPackageItemResponse>>builder()
                .result(modelPackageItemService.getByVehicleModelId(vehicleModelId))
                .build();
    }

    @GetMapping("/service-package/{servicePackageId}")
    public ApiResponse<List<ModelPackageItemResponse>> getByServicePackageId(
            @PathVariable Long servicePackageId) {
        return ApiResponse.<List<ModelPackageItemResponse>>builder()
                .result(modelPackageItemService.getByServicePackageId(servicePackageId))
                .build();
    }

    @GetMapping("/vehicle-model/{vehicleModelId}/package/{servicePackageId}")
    public ApiResponse<List<ModelPackageItemResponse>> getByVehicleModelAndPackage(
            @PathVariable Long vehicleModelId,
            @PathVariable Long servicePackageId) {
        return ApiResponse.<List<ModelPackageItemResponse>>builder()
                .result(modelPackageItemService.getByVehicleModelAndPackage(vehicleModelId, servicePackageId))
                .build();
    }

    @GetMapping("/vehicle-model/{vehicleModelId}/individual-services")
    public ApiResponse<List<ModelPackageItemResponse>> getIndividualServicesByModel(
            @PathVariable Long vehicleModelId) {
        return ApiResponse.<List<ModelPackageItemResponse>>builder()
                .result(modelPackageItemService.getIndividualServicesByModel(vehicleModelId))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ModelPackageItemResponse> updateModelPackageItem(
            @PathVariable Long id,
            @Valid @RequestBody ModelPackageItemRequest request) {
        return ApiResponse.<ModelPackageItemResponse>builder()
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
    @GetMapping("/total/model/{vehicleModelId}/package/{servicePackageId}")
    public ApiResponse<BigDecimal> getPackageTotalPrice(
            @PathVariable Long vehicleModelId,
            @PathVariable Long servicePackageId) {
        BigDecimal total = modelPackageItemService.getPackageTotalPrice(vehicleModelId, servicePackageId);
        return ApiResponse.<BigDecimal>builder()
                .result(total)
                .build();
    }

}
