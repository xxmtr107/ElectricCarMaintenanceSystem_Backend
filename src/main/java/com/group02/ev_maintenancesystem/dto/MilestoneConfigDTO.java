package com.group02.ev_maintenancesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneConfigDTO {
    private Integer milestoneKm;
    private Integer milestoneMonth;
}