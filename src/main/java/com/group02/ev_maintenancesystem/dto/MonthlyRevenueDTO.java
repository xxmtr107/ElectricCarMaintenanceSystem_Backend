package com.group02.ev_maintenancesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyRevenueDTO {
    private Integer month;
    private BigDecimal totalRevenue;
}
