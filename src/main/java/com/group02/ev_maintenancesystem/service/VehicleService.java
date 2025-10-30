package com.group02.ev_maintenancesystem.service;


import com.group02.ev_maintenancesystem.dto.request.VehicleCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.VehicleResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface VehicleService {
    VehicleResponse createVehicle(VehicleCreationRequest vehicleCreationRequest, Authentication authentication);
    List<VehicleResponse> getVehiclesByUserId(Long userId);
    VehicleResponse getVehiclesByVehicleId(Long vehicleId);
    Page<VehicleResponse> getAllVehicle(int size, int page);
    VehicleResponse updateVehicle(Long vehicleId, VehicleUpdateRequest request);
    VehicleResponse deleteVehicle(Long vehicleId);
}
