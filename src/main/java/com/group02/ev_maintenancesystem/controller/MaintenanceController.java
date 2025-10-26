package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/maintenance")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MaintenanceController {
    MaintenanceService maintenanceService;
    /**
     * Endpoint chính để lấy các đề xuất bảo dưỡng cho một xe.
     * * Cách dùng: GET http://localhost:8080/api/v1/maintenance/recommendations/1
     * (với 1 là vehicleId)
     */
    @GetMapping("/recommendations/{vehicleId}")
    public ApiResponse<List<MaintenanceRecommendationDTO>> getMaintenanceRecommendations(
            @PathVariable Long vehicleId) {

        return ApiResponse.<List<MaintenanceRecommendationDTO>>builder()
                .message("success")
                .result(maintenanceService.getRecommendations(vehicleId))
                .build();
    }


}
