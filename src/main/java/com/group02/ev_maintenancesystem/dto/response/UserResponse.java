package com.group02.ev_maintenancesystem.dto.response;

import com.group02.ev_maintenancesystem.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String fullName;
    // User info
    String username;
    String email;
    String phone;
    Gender gender;
    String role;
}
