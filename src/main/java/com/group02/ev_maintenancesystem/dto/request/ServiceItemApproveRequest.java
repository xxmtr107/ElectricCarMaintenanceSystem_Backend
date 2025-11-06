package com.group02.ev_maintenancesystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceItemApproveRequest {

    @NotNull(message = "NOT_BLANK")
    Long appointmentServiceDetailId; // ID của bản ghi AppointmentServiceItemDetail

    @NotNull(message = "NOT_BLANK")
    Boolean approved; // true (đồng ý) hoặc false (từ chối/quay về CHECK)
}