package com.group02.ev_maintenancesystem.dto.request;

import com.group02.ev_maintenancesystem.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;

    @NotBlank(message = "NOT_BLANK")
    String fullName;

    @NotBlank(message = "NOT_BLANK")
    @Email
    String email;

    @NotBlank(message = "NOT_BLANK")
    @Pattern(regexp = "^\\d{9,11}$", message = "PHONE_INVALID")
    String phone;

    @NotNull
    Gender gender;
}
