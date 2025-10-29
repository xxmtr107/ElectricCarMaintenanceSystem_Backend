package com.group02.ev_maintenancesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceItemDTO {
    private Long id;
    private String name;
    private String description;
}