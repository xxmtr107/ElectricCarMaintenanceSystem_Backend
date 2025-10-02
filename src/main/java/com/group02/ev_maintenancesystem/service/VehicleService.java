package com.group02.ev_maintenancesystem.service;


import com.group02.ev_maintenancesystem.dto.request.VehicleCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.VehicleResponse;

import java.util.List;

public interface VehicleService {
    VehicleResponse createVehicle(VehicleCreationRequest vehicleCreationRequest);
    List<VehicleResponse> getVehiclesByUserId(Long userId);
    VehicleResponse getVehiclesByVehicleId(Long vehicleId);
    List<VehicleResponse> getAllVehicle();
    VehicleResponse updateVehicle(Long vehicleId, VehicleUpdateRequest request);
    VehicleResponse deleteVehicle(Long vehicleId);
}
