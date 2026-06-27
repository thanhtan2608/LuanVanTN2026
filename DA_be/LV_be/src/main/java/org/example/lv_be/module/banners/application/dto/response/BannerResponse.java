package org.example.lv_be.module.banners.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BannerResponse {
    private Long id;
    private String title;
    private String imageUrl; // URL tĩnh lấy từ ImageKit
    private String targetUrl;
    private String position; // Trả về String cho Frontend dễ đọc
    private Integer displayOrder;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}