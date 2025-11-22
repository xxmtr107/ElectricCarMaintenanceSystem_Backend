package com.group02.ev_maintenancesystem.dto.response;

import com.group02.ev_maintenancesystem.enums.GeneralStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ServiceItemResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    GeneralStatus recordStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
