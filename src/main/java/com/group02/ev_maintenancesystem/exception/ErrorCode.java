package com.group02.ev_maintenancesystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Validation errors
    //User
    FULL_NAME_INVALID(997, "Full name must be 2 - 50 characters", HttpStatus.BAD_REQUEST),
    NOT_BLANK(998, "Field must not be blank", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1001, "Username must be 4 - 20 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    USER_EXISTED(1003, "User already exists", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS(1003, "User already exists", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1005, "Email already exists", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(1006, "Username already exists", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1007, "Phone number must be 9 - 11 digits", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1008, "Role not found", HttpStatus.NOT_FOUND),

    //Vehicles
    VIN_ALREADY_EXISTS(1009, "VIN already exists", HttpStatus.BAD_REQUEST),
    LICENSE_PLATE_ALREADY_EXISTS(1010, "License plate already exists", HttpStatus.BAD_REQUEST),
    MODEL_NOT_FOUND(1011, "Model not found", HttpStatus.NOT_FOUND),
    CUSTOMER_NOT_FOUND(1012, "Customer not found", HttpStatus.NOT_FOUND),
    USER_NOT_CUSTOMER(1013, "User is not customer", HttpStatus.BAD_REQUEST),
    VEHICLE_NOT_FOUND(1014, "Vehicle not found", HttpStatus.NOT_FOUND),
    VIN_LONG_INVALID(1015, "VIN must be exactly 17 characters long", HttpStatus.BAD_REQUEST),
    KILOMETER_LONG_INVALID(1016, "Kilometers must be between 0 and 999999", HttpStatus.BAD_REQUEST),
    LICENSE_PLATE_INVALID(1017, "License plate must have exactly 17 digit and number", HttpStatus.BAD_REQUEST),

    //VehicleModel
    MODEL_NAME_INVALID(1018, "Name of model must follow format like VFe34 or VF8", HttpStatus.BAD_REQUEST),
    MODEL_YEAR_INVALID(1019, "Vehile model year must between 2021 and 2025", HttpStatus.BAD_REQUEST),
    BASIC_MAINTENANCE_INVALID(1020, "Basic maintenance km must be between 1000 and 19999", HttpStatus.BAD_REQUEST),
    COMPREHENSIVE_MAINTENANCE_INVALID(1020, "Comprehensive maintenance km must be between 20000 and 999999", HttpStatus.BAD_REQUEST),
    BASIC_MAINTENANCE_TIME_INVALID(1021, "Basic maintenance time must be between 1 and 6", HttpStatus.BAD_REQUEST),
    COMPREHENSIVE_MAINTENANCE_TIME_INVALID(1022, "Conprehensive maintenance time must between 7 and 12", HttpStatus.BAD_REQUEST),
    VEHICLE_MODEL_NAME_DUPLICATE(1023, "Vehicle model name already exists", HttpStatus.BAD_REQUEST),
    VEHICLE_MODEL_NOT_FOUND(1024, "Vehicle model not found", HttpStatus.NOT_FOUND),

    // Authentication & Authorization
    UNAUTHENTICATED(1100, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    INVALID_KEY(1111, "Invalid key", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1112, "You do not have permission", HttpStatus.FORBIDDEN),

    // Server error
    INTERNAL_SERVER_ERROR(999, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED(996, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PATH_VARIABLE(995, "Invalid path variable", HttpStatus.BAD_REQUEST);




    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

}
