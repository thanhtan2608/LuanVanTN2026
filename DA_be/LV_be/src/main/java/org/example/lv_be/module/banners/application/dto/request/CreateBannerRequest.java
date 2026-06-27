package org.example.lv_be.module.banners.application.dto.request;

import lombok.Data;
import org.example.lv_be.module.banners.domain.entity.BannerPosition;

@Data
public class CreateBannerRequest {
    private String title;
    private String targetUrl;
    private BannerPosition position;
    private Integer displayOrder;
    // Không chứa MultipartFile ở đây, file sẽ được truyền rời từ Controller
}