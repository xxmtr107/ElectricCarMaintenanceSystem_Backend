package com.group02.ev_maintenancesystem.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EmailType {
    KM,
    APPOINTMENT_DATE,
    PAYMENT;

    @JsonCreator
    public static EmailType fromValue(String type){
        return EmailType.valueOf(type.trim().toUpperCase());
    }
}

