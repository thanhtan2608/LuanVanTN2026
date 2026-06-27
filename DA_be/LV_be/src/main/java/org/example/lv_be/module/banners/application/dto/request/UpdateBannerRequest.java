package org.example.lv_be.module.banners.application.dto.request;

import lombok.Data;
import org.example.lv_be.module.banners.domain.entity.BannerPosition;

@Data
public class UpdateBannerRequest {
    // Các trường này có thể null nếu Frontend không muốn cập nhật
    private String title;
    private String targetUrl;
    private BannerPosition position;
    private Integer displayOrder;
}