package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;

import java.util.List;

public interface MaintenanceService {
    List<MaintenanceRecommendationDTO> getRecommendations(Long vehicleId);


}