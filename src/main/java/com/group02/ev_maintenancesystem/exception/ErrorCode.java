package com.group02.ev_maintenancesystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // ========================= USER ERRORS (100 - 199) =========================
    FULL_NAME_INVALID(100, "Full name must be 2 - 50 characters", HttpStatus.BAD_REQUEST),
    NOT_BLANK(101, "Field must not be blank", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(102, "Username must be 4 - 20 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(103, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(104, "Email already exists", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(104, "Email invalid format", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(105, "Username already exists", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(106, "Phone number must be 9 - 11 digits", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(107, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(108, "User already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(109, "Role not found", HttpStatus.NOT_FOUND),
    USER_NOT_CUSTOMER(110, "User is not a customer", HttpStatus.BAD_REQUEST),

    // ========================= VEHICLE ERRORS (200 - 299) =========================
    VIN_ALREADY_EXISTS(200, "VIN already exists", HttpStatus.BAD_REQUEST),
    LICENSE_PLATE_ALREADY_EXISTS(201, "License plate already exists", HttpStatus.BAD_REQUEST),
    MODEL_NOT_FOUND(202, "Model not found", HttpStatus.NOT_FOUND),
    CUSTOMER_NOT_FOUND(203, "Customer not found", HttpStatus.NOT_FOUND),
    VEHICLE_NOT_FOUND(204, "Vehicle not found", HttpStatus.NOT_FOUND),
    VIN_INVALID_LENGTH(205, "VIN must be exactly 17 characters long", HttpStatus.BAD_REQUEST),
    KILOMETER_INVALID_RANGE(206, "Kilometers must be between 0 and 999999", HttpStatus.BAD_REQUEST),
    LICENSE_PLATE_INVALID(207, "License plate format is invalid", HttpStatus.BAD_REQUEST),
    VEHICLE_NOT_BELONG_TO_CUSTOMER(208, "Vehicle does not belong to the customer", HttpStatus.BAD_REQUEST),

    // ========================= APPOINTMENT ERRORS (300 - 399) =========================
    APPOINTMENT_NOT_FOUND(300, "Appointment not found", HttpStatus.NOT_FOUND),
    APPOINTMENT_ALREADY_EXISTS(301, "Appointment already exists", HttpStatus.BAD_REQUEST),
    TECHNICIAN_NOT_AVAILABLE(302, "Technician is not available", HttpStatus.BAD_REQUEST),
    STATUS_INVALID(303, "Invalid appointment status", HttpStatus.BAD_REQUEST),
    DATE_INVALID(303, "Invalid appointment date", HttpStatus.BAD_REQUEST),
    // ========================= AUTHENTICATION & AUTHORIZATION (400 - 499) =========================
    UNAUTHENTICATED(400, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    INVALID_KEY(401, "Invalid key", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),

    // ========================= SERVER ERRORS (900 - 999) =========================
    INTERNAL_SERVER_ERROR(900, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED(901, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR);

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

}
