package org.example.lv_be.module.ailookbook.domain.enums;

public enum AiProcessStatus {
    PENDING,      // Đang xếp hàng chờ xử lý
    PROCESSING,   // AI đang thực hiện vẽ/ghép tóc
    SUCCESS,      // Xử lý thành công
    FAILED        // Xử lý thất bại (Lỗi API, ảnh lỗi...)
}