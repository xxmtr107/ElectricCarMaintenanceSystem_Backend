package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "NOT_BLANK")
    @Email(message = "EMAIL_INVALID")
    private String email;
}
