package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordUpdateRequest {

    @NotBlank(message = "NOT_BLANK")
    String oldPassword; // Mật khẩu cũ

    @NotBlank(message = "NOT_BLANK")
    @Size(min = 8, message = "PASSWORD_INVALID") // Validate độ dài mật khẩu mới
    String newPassword; // Mật khẩu mới
}