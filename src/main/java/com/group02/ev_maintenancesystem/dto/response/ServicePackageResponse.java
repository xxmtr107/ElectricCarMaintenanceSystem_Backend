package com.group02.ev_maintenancesystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicePackageResponse {
    Long id;
    String name;
    BigDecimal price;
    String description;
    LocalDateTime createdAt;
    String createBy;
    LocalDateTime updateAt;
    String updateBy;

}
