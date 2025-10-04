package com.group02.ev_maintenancesystem.dto.request;

import com.group02.ev_maintenancesystem.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TechnicianRegistrationRequest {
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
    @Email(message = "EMAIL_INVALID")
    String email;

    @NotBlank(message = "NOT_BLANK")
    @Pattern(regexp = "^\\d{9,11}$", message = "PHONE_INVALID")
    String phone;

    @NotNull(message = "NOT_BLANK")
    Gender gender;

    @NotBlank(message = "NOT_BLANK")
    String specialization;

    @NotNull(message = "NOT_BLANK")
    Integer experienceYears;
}
