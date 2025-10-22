package com.group02.ev_maintenancesystem.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentStatus {
    UN_PAID,
    PAID,
    CANCELLED,
    FAILED;

    @JsonCreator
    public static PaymentStatus fromPaymentStatus(String value) {
        return PaymentStatus.valueOf(value.trim().toUpperCase());
    }
}
