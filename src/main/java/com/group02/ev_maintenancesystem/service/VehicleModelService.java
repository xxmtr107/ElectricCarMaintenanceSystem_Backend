package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.VehicleModelRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleModelUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.VehicleModelGetResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VehicleModelService {
    VehicleModelGetResponse createVehicleModel(VehicleModelRequest request);

    VehicleModelGetResponse getVehicleModelById(Long vehicleModelId);

    List<VehicleModelGetResponse> getAllVehicleModel();

    Page<VehicleModelGetResponse> searchVehicleModelByName(String keyword, int page, int size);
    VehicleModelGetResponse updateVehicleMode(Long vehicleModelId, VehicleModelUpdateRequest request);
    VehicleModelGetResponse deleteVehicleModelById(Long vehicleId);
}
