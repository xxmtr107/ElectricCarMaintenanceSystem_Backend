package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "NOT_BLANK")
    private String token;

    @NotBlank(message = "NOT_BLANK")
    @Size(min = 8, message = "PASSWORD_INVALID")
    private String newPassword;

    @NotBlank(message = "NOT_BLANK")
    private String confirmPassword;
}
