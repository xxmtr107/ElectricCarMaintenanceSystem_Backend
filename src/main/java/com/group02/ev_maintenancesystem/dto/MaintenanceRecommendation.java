package com.group02.ev_maintenancesystem.dto;

import com.group02.ev_maintenancesystem.entity.ModelPackageItem;
import com.group02.ev_maintenancesystem.entity.ServicePackage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO này chứa một đề xuất bảo dưỡng hoàn chỉnh
 */
@Getter
@Setter
@AllArgsConstructor
public class MaintenanceRecommendation {

    // Gói được đề xuất (ví dụ: "Bảo dưỡng Tiêu chuẩn")
    private ServicePackage recommendedPackage;

    // Đề xuất cho mốc nào
    private int dueAtKm;
    private int dueAtMonths;

    // Lý do đề xuất
    private String reason; // "DUE_BY_KM", "DUE_BY_DATE", "OVERDUE_KM", "OVERDUE_DATE"

    // Danh sách các hạng mục (để hiển thị cho khách)
    private List<ModelPackageItem> items;
}