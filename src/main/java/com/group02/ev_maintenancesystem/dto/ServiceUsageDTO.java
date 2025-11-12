package com.group02.ev_maintenancesystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceUsageDTO {
    private Long serviceItemId;
    private String serviceItemName;
    private Long usageCount;
}