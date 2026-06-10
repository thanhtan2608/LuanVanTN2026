package org.example.lv_be.module.ailookbook.domain.entity;

import lombok.*;
import org.example.lv_be.module.ailookbook.domain.enums.GenderCategory;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LookbookItem {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;    // Ảnh mẫu do AI gen sẵn để làm bộ sưu tập (Gallery)
    private GenderCategory gender;
    private String prompt;      // Câu lệnh cấu hình cho AI lồng ghép mẫu tóc này
    private Long hairstyleId;   // CẦU NỐI THỰC TẾ: Trỏ sang kiểu tóc thực tế tại tiệm (Ví dụ: Layer, Undercut)
    private boolean active;
    private boolean deleted;
    private LocalDateTime createdAt;

    // ==========================================
    // Logic Nghiệp Vụ Nội Tại (Rich Domain)
    // ==========================================

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    /**
     * Đồng bộ hóa hình ảnh mẫu AI này với một kiểu tóc thực tế có trong tiệm
     */
    public void linkToActualHairstyle(Long realHairstyleId) {
        if (realHairstyleId == null || realHairstyleId <= 0) {
            throw new IllegalArgumentException("Mã kiểu tóc thực tế liên kết không hợp lệ!");
        }
        this.hairstyleId = realHairstyleId;
    }

    /**
     * Cập nhật lại câu lệnh cấu hình AI (Prompt)
     */
    public void updatePrompt(String newPrompt) {
        if (newPrompt == null || newPrompt.isBlank()) {
            throw new IllegalArgumentException("Câu lệnh Prompt cấu hình cho AI Engine không được để trống!");
        }
        this.prompt = newPrompt;
    }
}