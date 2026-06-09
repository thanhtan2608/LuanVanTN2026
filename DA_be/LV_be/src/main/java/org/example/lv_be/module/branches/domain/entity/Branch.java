package org.example.lv_be.module.branches.domain.entity;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean isActive;
    private boolean isDeleted;
    private LocalDateTime createdAt;

    // ==========================================
    // Các logic nghiệp vụ nội tại (Business Rules)
    // ==========================================

    /**
     * Kích hoạt chi nhánh hoạt động trở lại
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Tạm ngưng hoạt động chi nhánh
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Kiểm tra xem hiện tại chi nhánh có đang trong giờ phục vụ hay không
     */
    public boolean isCurrentlyOpen(LocalTime checkTime) {
        if (!this.isActive || this.isDeleted) {
            return false;
        }
        // Trường hợp giờ mở cửa trước giờ đóng cửa (Ví dụ: 08:00 -> 21:00)
        if (openTime.isBefore(closeTime)) {
            return !checkTime.isBefore(openTime) && !checkTime.isAfter(closeTime);
        }
        // Trường hợp chi nhánh mở xuyên đêm (Ví dụ: 22:00 -> 03:00 sáng hôm sau)
        return !checkTime.isBefore(openTime) || !checkTime.isAfter(closeTime);
    }
}