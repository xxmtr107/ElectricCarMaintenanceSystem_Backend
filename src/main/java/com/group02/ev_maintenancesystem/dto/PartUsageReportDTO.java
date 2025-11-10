package com.group02.ev_maintenancesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartUsageReportDTO {
    private Long sparePartId;
    private String sparePartName;
    private String sparePartNumber;
    private Long totalQuantityUsed;
}