package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicePackageCreationRequest {
    @NotBlank(message ="NOT_BLANK")
    @Size(min = 10, max = 100, message = "SERVICE_PACKAGE_NAME_INVALID")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s\\-/,:]+$", message ="SERVICE_PACKAGE_NAME_INVALID")
    String name;

    @NotBlank(message ="NOT_BLANK")
    @Size(min = 10, max = 150, message = "SERVICE_DESCRIPTION_INVALID")
    String description;

    @NotNull(message ="NOT_BLANK")
    @DecimalMin(value = "0.0", inclusive = false, message = "SERVICE_PRICE_INVALID")
    BigDecimal price;

}
