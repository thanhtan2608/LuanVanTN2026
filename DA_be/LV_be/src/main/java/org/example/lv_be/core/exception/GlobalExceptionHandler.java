package org.example.lv_be.core.exception;

import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.users.domain.exception.UserDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Bắt lỗi nghiệp vụ do chính chúng ta chủ động ném ra (AppException)
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.error(ex.getStatus().value(), ex.getMessage()));
    }

    // 2. Bắt lỗi khi Frontend gửi thiếu dữ liệu hoặc sai định dạng (Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, "Dữ liệu đầu vào không hợp lệ: " + errors.toString()));
    }

    // 3. Bắt TẤT CẢ các lỗi hệ thống chưa lường trước được (Null Pointer, Đứt cáp DB...)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        // Trong thực tế, bạn nên dùng thư viện Log (như Slf4j) để in lỗi ra console ở đây
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Lỗi máy chủ nội bộ. Vui lòng thử lại sau!"));
    }
    @ExceptionHandler(UserDomainException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserDomainException(UserDomainException ex) {
        // Lỗi nghiệp vụ Domain mặc định sẽ trả về HTTP 400 (Bad Request)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, ex.getMessage()));
    }
}