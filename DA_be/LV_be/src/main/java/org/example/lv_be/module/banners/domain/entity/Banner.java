package org.example.lv_be.module.banners.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Banner {
    private Long id;
    private String title;
    private String imageUrl;
    private String targetUrl;
    private BannerPosition position;
    private Integer displayOrder;

    @Builder.Default
    private boolean isActive = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 🌟 Logic nghiệp vụ gắn liền với Entity: Tự đảo ngược trạng thái bật/tắt
    public void toggleActive() {
        this.isActive = !this.isActive;
    }
}