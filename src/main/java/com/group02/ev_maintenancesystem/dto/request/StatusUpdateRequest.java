package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusUpdateRequest {
    @NotNull(message = "STATUS_REQUIRED")
    Boolean isActive; // true: Kích hoạt, false: Ngừng hoạt động
}