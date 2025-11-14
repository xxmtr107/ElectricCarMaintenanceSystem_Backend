package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceItemCreationRequest {

    @NotBlank(message ="NOT_BLANK")
    @Size(min = 10, max = 100, message = "SERVICE_ITEM_NAME_INVALID")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s\\-/,:]+$", message="SERVICE_NAME_INVALID")
    private String name;

    @NotBlank(message = "NOT_BLANK")
    @Size(min = 10, max = 150, message = "SERVICE_DESCRIPTION_INVALID")
    private String description;
}
