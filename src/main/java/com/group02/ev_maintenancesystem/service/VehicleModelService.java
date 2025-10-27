package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.VehicleModelRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleModelUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.VehicleModelResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VehicleModelService {
    VehicleModelResponse createVehicleModel(VehicleModelRequest request);

    VehicleModelResponse getVehicleModelById(Long vehicleModelId);

    List<VehicleModelResponse> getAllVehicleModel();

    Page<VehicleModelResponse> searchVehicleModelByName(String keyword, int page, int size);
    VehicleModelResponse updateVehicleModel(Long vehicleModelId, VehicleModelUpdateRequest request);
    void deleteVehicleModelById(Long vehicleId);
}
