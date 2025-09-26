package com.group02.ev_maintenancesystem.exception;

import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global Exception Handler - Xử lý tất cả exception xảy ra trong ứng dụng
 * @ControllerAdvice: Annotation này cho phép class này "lắng nghe" tất cả exception
 * từ mọi controller trong ứng dụng và xử lý chúng một cách tập trung
 */
@ControllerAdvice
public class GlobalExceptionHandle {

    /**
     * Xử lý tất cả RuntimeException chưa được handle cụ thể
     * Đây là "safety net" cuối cùng để catch các lỗi không mong muốn
     *
     * @param ex RuntimeException hoặc subclass của nó
     * @return ResponseEntity với HTTP 400 Bad Request
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRunTimeException(RuntimeException ex){
        // Tạo response với error code UNCATEGORIZED cho các lỗi không xác định
        ApiResponse response = new ApiResponse();
        response.setCode(ErrorCode.UNCATEGORIZED.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED.getMessage());

        // Trả về HTTP 400 Bad Request với body là ApiResponse
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Xử lý các custom AppException - những exception được định nghĩa trong ứng dụng
     * Đây là cách chính để handle business logic errors
     *
     * @param ex AppException chứa ErrorCode cụ thể
     * @return ResponseEntity với HTTP status code tương ứng với ErrorCode
     */
    @ExceptionHandler (value = AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException ex){
        // Lấy ErrorCode từ AppException để biết loại lỗi cụ thể
        ErrorCode errorCode = ex.getErrorCode();

        ApiResponse response = new ApiResponse();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        // Sử dụng HTTP status code được định nghĩa trong ErrorCode
        // Ví dụ: USER_NOT_FOUND -> 404, INVALID_CREDENTIALS -> 401
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }

    /**
     * Xử lý validation errors từ @Valid annotation
     * Khi request body không pass validation (@NotNull, @Size, etc.),
     * Spring sẽ throw MethodArgumentNotValidException
     *
     * @param ex MethodArgumentNotValidException chứa thông tin validation error
     * @return ResponseEntity với HTTP 400 Bad Request
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex){
        // Lấy message từ validation annotation, ví dụ: @NotNull(message = "USERNAME_INVALID")
        String enumKey = ex.getFieldError().getDefaultMessage();

        // Convert string thành ErrorCode enum
        // Yêu cầu: validation message phải match với tên ErrorCode enum
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);

        ApiResponse response = new ApiResponse();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Xử lý AccessDeniedException từ Spring Security
     * Xảy ra khi user đã authenticate nhưng không có quyền truy cập resource
     * (khác với authentication failure - chưa đăng nhập)
     *
     * @param ex AccessDeniedException từ Spring Security
     * @return ResponseEntity với HTTP 403 Forbidden
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDenied(AccessDeniedException ex){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        // Sử dụng Builder pattern để tạo ApiResponse (cách viết hiện đại hơn)
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}