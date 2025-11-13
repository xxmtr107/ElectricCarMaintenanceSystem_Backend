package com.group02.ev_maintenancesystem.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EmailType {
    KM,
    APPOINTMENT_CONFIRMATION,
    APPOINTMENT_CONFIRMED_AND_ASSIGNED_SUCCESS,
    APPOINTMENT_REMINDER,
    PAYMENT_REMINDER,
    PAYMENT_CONFIRMATION,
    ACCOUNT_CREATION;



    @JsonCreator
    public static EmailType fromValue(String type){
        return EmailType.valueOf(type.trim().toUpperCase());
    }
}

