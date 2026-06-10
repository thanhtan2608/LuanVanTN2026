package org.example.lv_be.module.hairstyles.domain.entity;

import lombok.*;
// 🌟 Import trực tiếp enum dáng khuôn mặt đã có sẵn ở gói common của hai bạn
import org.example.lv_be.common.enums.FaceShape;
import org.example.lv_be.module.hairstyles.domain.exception.HairstyleDomainException;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hairstyle {
    private Long id;                  // bigint(20) AUTO_INCREMENT
    private String name;              // varchar(255)
    private FaceShape faceShape;      // enum('OVAL', 'ROUND', 'SQUARE', 'LONG', 'HEART',...) từ common
    private String imageUrl;          // varchar(500)
    private String description;       // text
    private LocalDateTime createdAt;  // timestamp DEFAULT CURRENT_TIMESTAMP
    private boolean deleted;          // tinyint(1) -> Ánh xạ từ cột is_deleted
    private boolean active;           // tinyint(1) -> Ánh xạ từ cột is_active

    /**
     * Kiểm tra tính toàn vẹn và hợp lệ của dữ liệu kiểu tóc
     */
    public void validateSelf() {
        if (this.name == null || this.name.isBlank()) {
            throw new HairstyleDomainException("Tên kiểu tóc không được phép để trống!");
        }
        if (this.faceShape == null) {
            throw new HairstyleDomainException("Kiểu tóc bắt buộc phải gán với một dáng khuôn mặt phù hợp!");
        }
    }

    /**
     * Nghiệp vụ xóa mềm kiểu tóc ra khỏi hệ thống hiển thị ứng dụng
     */
    public void softDelete() {
        this.deleted = true;
        this.active = false;
    }
}