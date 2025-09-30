package com.group02.ev_maintenancesystem.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    @JsonCreator
    public static Gender fromJson(String value) {
        return Gender.valueOf(value.trim().toUpperCase());
    }
}
