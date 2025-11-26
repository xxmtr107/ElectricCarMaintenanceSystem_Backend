package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.MaintenanceRecommendationDTO;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.service.MaintenanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;


@RestController
@RequestMapping("/maintenance")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaintenanceController {
    MaintenanceService maintenanceService;

    /**
     * Lấy đề xuất bảo dưỡng.
     * Thêm param 'currentKm': Khách hàng nhập số ODO hiện tại trên đồng hồ để hệ thống tính toán.
     * Giá trị này chỉ dùng để tính toán, KHÔNG update vào DB xe.
     */
    @GetMapping("/recommendations/{vehicleId}")
    public ApiResponse<List<MaintenanceRecommendationDTO>> getMaintenanceRecommendations(
            @PathVariable Long vehicleId,
            @RequestParam(required = false) Integer currentOdo // [THÊM MỚI] Tham số optional
    ) {
        return ApiResponse.<List<MaintenanceRecommendationDTO>>builder()
                .message("Success")
                // Truyền thêm currentOdo vào service
                .result(maintenanceService.getRecommendations(vehicleId, currentOdo))
                .build();
    }
}
