package com.group02.ev_maintenancesystem.dto.response;

import com.group02.ev_maintenancesystem.enums.Gender;
import com.group02.ev_maintenancesystem.enums.GeneralStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
    Long serviceCenterId;
    String serviceCenterName;
    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String createdBy;
    String updatedBy;
}
