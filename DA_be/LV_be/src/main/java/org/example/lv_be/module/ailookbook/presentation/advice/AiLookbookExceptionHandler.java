package org.example.lv_be.module.ailookbook.presentation.advice;

import lombok.extern.slf4j.Slf4j;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.ailookbook.domain.exception.AiLookbookDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * =========================================================================
 * 📘 TÀI LIỆU SỰ CỐ DÀNH CHO FRONTEND (ERROR PAYLOAD DOCUMENTATION)
 * =========================================================================
 * Khi xảy ra sự cố (Sai từ khóa tóc, lỗi ảnh, không tìm thấy ID...), API sẽ
 * KHÔNG sập hoàn toàn mà trả về mã HTTP Status tương ứng (400, 404, 500)
 * kèm cấu trúc JSON chuẩn hóa bên dưới để Frontend tiện bắt lỗi hiển thị Alert:
 * * {
 * "success": false,
 * "code": 400,
 * "message": "Thông báo chi tiết nguyên nhân lỗi ở đây...",
 * "data": null
 * }
 * =========================================================================
 */
@Slf4j
@RestControllerAdvice(basePackages = "org.example.lv_be.module.ailookbook.presentation.rest")
public class AiLookbookExceptionHandler {

    /**
     * Bẫy các lỗi nghiệp vụ nội tại (Ví dụ: Thao tác sai trạng thái AI, không tìm thấy item)
     * Trả về HTTP Status: 400 Bad Request hoặc 404 Not Found tùy ngữ cảnh
     */
    @ExceptionHandler(AiLookbookDomainException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainException(AiLookbookDomainException ex) {
        log.warn("🚨 [AI LOOKBOOK ERROR] Lỗi nghiệp vụ logic: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    /**
     * Bẫy lỗi tối thượng từ hệ thống hoặc lỗi từ đối tác thứ 3 (Google API sập, ImageKit lỗi,
     * hoặc câu lệnh Prompt bị Hàng rào bảo vệ chặn do không chứa từ khóa chuyên ngành tóc).
     * Trả về HTTP Status: 500 Internal Server Error hoặc 400 Bad Request
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
        log.error("🚨 [SYSTEM CRITICAL ERROR] Phát hiện sự cố luồng chạy ngầm: ", ex);

        // Nếu lỗi do Hàng rào bảo vệ chặn từ khóa tinh chỉnh, ép về mã 400 để Frontend biết do khách nhập sai
        if (ex.getMessage() != null && ex.getMessage().contains("từ chối xử lý")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }
}