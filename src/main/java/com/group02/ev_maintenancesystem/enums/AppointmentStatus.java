package com.group02.ev_maintenancesystem.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AppointmentStatus {
    CANCELLED,
    COMPLETED,
    PENDING,
    CONFIRMED,
    WAITING_FOR_APPROVAL;

    @JsonCreator
    public static AppointmentStatus fromJson(String value) {
        return AppointmentStatus.valueOf(value.trim().toUpperCase());
    }
}
