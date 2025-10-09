package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.VehicleModelRequest;
import com.group02.ev_maintenancesystem.dto.response.VehicleModelCreationResponse;
import com.group02.ev_maintenancesystem.dto.response.VehicleModelGetResponse;
import com.group02.ev_maintenancesystem.dto.response.VehicleModelUpdateResponse;

import java.util.List;

public interface VehicleModelService {
    VehicleModelCreationResponse createVehicleModel(VehicleModelRequest request);
    VehicleModelCreationResponse getVehicleModelById(Long vehicleModelId);
    List<VehicleModelGetResponse> getAllVehicleModel();
    List<VehicleModelGetResponse> searchVehicleModelByName(String keyword);
    VehicleModelUpdateResponse updateVehicleMode(Long vehicleModelId, VehicleModelRequest request);
    VehicleModelGetResponse deleteVehicleModelById(Long vehicleId);
}
