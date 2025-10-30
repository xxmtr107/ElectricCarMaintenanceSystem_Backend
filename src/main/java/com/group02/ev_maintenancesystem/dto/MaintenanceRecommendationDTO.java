package com.group02.ev_maintenancesystem.dto;

// (Import các DTO mới)
import com.group02.ev_maintenancesystem.enums.MaintenanceActionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceRecommendationDTO {

//    private Long packageId;
//    private String packageName;

    private Integer milestoneKm;

    // Các thông tin mốc
    private int dueAtKm;
    private int dueAtMonths;
    private String reason;

    // --- THAY ĐỔI QUAN TRỌNG ---
    // Không dùng Entity "List<ModelPackageItem>"
    // Dùng DTO "List<ModelPackageItemDTO>"
    private List<ModelPackageItemDTO> items;
    private BigDecimal estimatedTotal;
}