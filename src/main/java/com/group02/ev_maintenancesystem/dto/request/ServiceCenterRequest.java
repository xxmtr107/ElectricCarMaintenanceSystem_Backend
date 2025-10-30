package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCenterRequest {

    @NotBlank(message = "NOT_BLANK")
    @Size(min = 5, max = 255, message = "FIELD_INVALID") // Using a generic "FIELD_INVALID"
    private String name;

    @NotBlank(message = "NOT_BLANK")
    @Size(min = 10, max = 255, message = "FIELD_INVALID")
    private String address;

    @NotBlank(message = "NOT_BLANK")
    @Size(min = 2, max = 100, message = "FIELD_INVALID")
    private String district;

    @NotBlank(message = "NOT_BLANK")
    @Size(min = 2, max = 100, message = "FIELD_INVALID")
    private String city;

    @NotBlank(message = "NOT_BLANK")
    @Pattern(regexp = "^\\d{9,11}$", message = "PHONE_INVALID")
    private String phone;
}