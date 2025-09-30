package com.group02.ev_maintenancesystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Validation errors
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

    // Authentication & Authorization
    UNAUTHENTICATED(1009, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    INVALID_KEY(1010, "Invalid key", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1011, "You do not have permission", HttpStatus.FORBIDDEN),

    // Server error
    INTERNAL_SERVER_ERROR(999, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED(996, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR);



    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
    private int code;
    private String message;
    private HttpStatus httpStatus;

}
