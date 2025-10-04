package com.group02.ev_maintenancesystem.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().trim();

        try {
            if (value.length() == 10) {
                return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            } else if (value.length() == 13) {
                return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
            } else if (value.length() == 16) {
                return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } else {
                return LocalDateTime.parse(value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format! Use yyyy-MM-dd, yyyy-MM-dd HH, or yyyy-MM-dd HH:mm");
        }
    }
}
