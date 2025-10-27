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
    EMAIL_INVALID(105, "Email invalid format", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(106, "Username already exists", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(107, "Phone number must be 9 - 11 digits", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(108, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(109, "User already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(110, "Role not found", HttpStatus.NOT_FOUND),
    USER_NOT_CUSTOMER(111, "User is not a customer", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_SAME_AS_OLD(112, "New password same as old password", HttpStatus.BAD_REQUEST),
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
    CANNOT_DELETE_VEHICLE_WITH_ACTIVE_APPOINTMENT(209, "Cannot delete vehicle because it has active appointments (not completed or canceled).", HttpStatus.BAD_REQUEST),

    // ========================= VEHICLE MODEL ERRORS (300 - 399) =========================
    MODEL_NAME_INVALID(300, "Name of model must follow format like VFe34 or VF8", HttpStatus.BAD_REQUEST),
    MODEL_YEAR_INVALID(301, "Vehicle model year must be between 2021 and 2025", HttpStatus.BAD_REQUEST),
    BASIC_MAINTENANCE_INVALID(302, "Basic maintenance km must be between 1000 and 19999", HttpStatus.BAD_REQUEST),
    COMPREHENSIVE_MAINTENANCE_INVALID(303, "Comprehensive maintenance km must be between 20000 and 999999", HttpStatus.BAD_REQUEST),
    BASIC_MAINTENANCE_TIME_INVALID(304, "Basic maintenance time must be between 1 and 6", HttpStatus.BAD_REQUEST),
    COMPREHENSIVE_MAINTENANCE_TIME_INVALID(305, "Comprehensive maintenance time must be between 7 and 12", HttpStatus.BAD_REQUEST),
    VEHICLE_MODEL_NAME_DUPLICATE(306, "Vehicle model name already exists", HttpStatus.BAD_REQUEST),
    VEHICLE_MODEL_NOT_FOUND(307, "Vehicle model not found", HttpStatus.NOT_FOUND),


    // ========================= APPOINTMENT ERRORS (400 - 499) =========================
    APPOINTMENT_NOT_FOUND(400, "Appointment not found", HttpStatus.NOT_FOUND),
    APPOINTMENT_ALREADY_EXISTS(401, "Appointment already exists", HttpStatus.BAD_REQUEST),
    TECHNICIAN_NOT_AVAILABLE(402, "Technician is not available", HttpStatus.BAD_REQUEST),
    STATUS_INVALID(403, "Invalid appointment status", HttpStatus.BAD_REQUEST),
    DATE_INVALID(404, "Invalid appointment date", HttpStatus.BAD_REQUEST),
    INVALID_DATE_RANGE(405, "Start date must be before end date", HttpStatus.BAD_REQUEST),
    DUPLICATE_SERVICE_ITEMS(406, "Duplicated service items", HttpStatus.BAD_REQUEST),
    CANNOT_CANCEL_COMPLETED_APPOINTMENT(407, "Cannot cancel completed appointment", HttpStatus.BAD_REQUEST),
    APPOINTMENT_ALREADY_CANCELLED(408, "Appointment is already cancelled", HttpStatus.BAD_REQUEST),
    TECHNICIAN_NOT_ASSIGNED(409, "Technician not assigned", HttpStatus.BAD_REQUEST),

    // ========================= AUTHENTICATION & AUTHORIZATION (500 - 599) =========================
    UNAUTHENTICATED(500, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    INVALID_KEY(501, "Invalid key", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(502, "You do not have permission", HttpStatus.FORBIDDEN),

    // ========================= SERVICE ITEMS & SERVICE PACKAGES (600 - 699) =========================
    MUST_SELECT_SERVICE(600, "Must select at least one service package or service item", HttpStatus.BAD_REQUEST),
    SERVICE_PACKAGE_NOT_FOUND(601, "Service package not found", HttpStatus.BAD_REQUEST),
    SERVICE_ITEM_NOT_FOUND(602, "Service item not found", HttpStatus.NOT_FOUND),
    SERVICE_ITEM_NAME_INVALID(603, "Service must between 10-100 and just contain / or : ", HttpStatus.BAD_REQUEST),
    SERVICE_PRICE_INVALID(604, "Price must be greater than 0", HttpStatus.BAD_REQUEST),
    SERVICE_ITEM_NAME_DUPLICATE(605, "Service item name already exists", HttpStatus.BAD_REQUEST),
    SERVICE_DESCRIPTION_INVALID(606, "Description must between 10-150", HttpStatus.BAD_REQUEST),
    SERVICE_PACKAGE_NAME_INVALID(607, "Package name must between 10-100 and just contain / or :", HttpStatus.BAD_REQUEST),
    SERVICE_PACKAGE_NAME_DUPLICATE(608, "Service package name already exists",HttpStatus.BAD_REQUEST),

    // ========================= PAYMENT ERRORS (700 - 799) =========================
    FIELD_INVALID(701, "Invalid field", HttpStatus.BAD_REQUEST),
    HASH_DATA_FAIL(703, "Hash data fail", HttpStatus.INTERNAL_SERVER_ERROR),
    SIGNATURE_INVALID(704, "Invalid VNPay signature", HttpStatus.BAD_REQUEST),
    PAYMENT_FAILED(705, "Payment failed", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_FOUND(706, "Payment not found", HttpStatus.NOT_FOUND),
    CREATE_PAYMENT_FAILD(707, "Create payment failed", HttpStatus.INTERNAL_SERVER_ERROR),

    // ========================= INVOICE ERRORS (800 - 899) =========================
    INVOICE_NOT_FOUND(800, "Invoice not found for this appointment", HttpStatus.BAD_REQUEST),
    INVOICE_ALREADY_PAID(801, "Invoice already paid", HttpStatus.BAD_REQUEST), // THÊM MÃ LỖI NÀY

    // ========================= MODEL PACKAGE ITEM ERRORS (900 - 999) =========================
    MODEL_PACKAGE_ITEM_NOT_FOUND(900, "Model package item not found", HttpStatus.NOT_FOUND),
    MODEL_PACKAGE_ITEM_EXISTED(901, "Model package item already exists", HttpStatus.BAD_REQUEST),
    PRICE_INVALID(902, "Price must be greater than 0", HttpStatus.BAD_REQUEST),


    // ========================= SERVER ERRORS (1000 - 1099) =========================
    INTERNAL_SERVER_ERROR(1000, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED(1001, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),


    MAINTENANCE_RECORD_NOT_FOUND(1100, "Maintenance record not found", HttpStatus.NOT_FOUND);
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
