package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendation;
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
    public ApiResponse<List<MaintenanceRecommendation>> getMaintenanceRecommendations(
            @PathVariable Long vehicleId) {

        // 1. Gọi service "bộ não"
        List<MaintenanceRecommendation> recommendations = maintenanceService.getRecommendations(vehicleId);

        // 2. Trả về kết quả (danh sách có thể rỗng nếu không có gì đến hạn)
        return ApiResponse.<List<MaintenanceRecommendation>>builder()
                .message("success")
                .result(maintenanceService.getRecommendations(vehicleId))
                .build();
    }


}
