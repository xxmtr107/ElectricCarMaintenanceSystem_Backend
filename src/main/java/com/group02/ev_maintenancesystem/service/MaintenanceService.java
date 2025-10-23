package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.entity.MaintenanceRecord;
import com.group02.ev_maintenancesystem.entity.ServicePackage;
import com.group02.ev_maintenancesystem.entity.Vehicle;

import java.util.List;
import java.util.Optional;

public interface MaintenanceService {
    List<MaintenanceRecommendationDTO> getRecommendations(Long vehicleId);

    Optional<MaintenanceRecommendationDTO> checkRule(
            Vehicle vehicle,
            List<MaintenanceRecord> fullHistory,
            ServicePackage packageToCheck,
            Integer cycleKm,
            Integer cycleMonths);
}
