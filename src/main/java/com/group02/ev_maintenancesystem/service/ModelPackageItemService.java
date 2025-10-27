package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.ModelPackageItemRequest;
import com.group02.ev_maintenancesystem.dto.response.ModelPackageItemResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ModelPackageItemService {


    ModelPackageItemResponse createModelPackageItem(ModelPackageItemRequest request);
    BigDecimal getMilestoneTotalPrice(Long vehicleModelId, Integer milestoneKm);
    void deleteModelPackageItem(Long id);
    ModelPackageItemResponse updateModelPackageItem(Long id, ModelPackageItemRequest request);
    List<ModelPackageItemResponse> getByVehicleModelAndMilestoneKm(Long vehicleModelId, Integer milestoneKm);
    List<ModelPackageItemResponse> getByVehicleModelId(Long vehicleModelId);
    List<ModelPackageItemResponse> getAllModelPackageItems();
    ModelPackageItemResponse getModelPackageItemById(Long id);

}


