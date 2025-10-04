package com.group02.ev_maintenancesystem.dto.response;

import com.group02.ev_maintenancesystem.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TechnicianResponse {
    Long id;
    String fullName;
    String username;
    String email;
    String phone;
    Gender gender;
    String specialization;
    Integer experienceYears;
    String role;

}
