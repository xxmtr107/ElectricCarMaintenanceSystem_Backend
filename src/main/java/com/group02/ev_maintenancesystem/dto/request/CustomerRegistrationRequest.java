package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRegistrationRequest {
    @NotBlank(message = "NOT_BLANK")
    @Size(min = 4, max = 20, message = "USERNAME_INVALID")
    String username;

    @NotBlank(message = "NOT_BLANK")
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "NOT_BLANK")
    @Size(min = 2, max = 50, message = "FULL_NAME_INVALID")
    String fullName;

    @NotBlank(message = "NOT_BLANK")
    @Email
    String email;

    @NotBlank(message = "NOT_BLANK")
    @Pattern(regexp = "^\\d{9,11}$", message = "PHONE_INVALID")
    String phone;

    @NotBlank(message = "NOT_BLANK")
    String gender;
}
