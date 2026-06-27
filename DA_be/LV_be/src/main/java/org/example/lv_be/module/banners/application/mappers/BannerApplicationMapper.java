package org.example.lv_be.module.banners.application.mappers;

import org.example.lv_be.module.banners.application.dto.response.BannerResponse;
import org.example.lv_be.module.banners.domain.entity.Banner;

public class BannerApplicationMapper {

    public static BannerResponse toResponse(Banner banner) {
        if (banner == null) return null;

        return BannerResponse.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .imageUrl(banner.getImageUrl())
                .targetUrl(banner.getTargetUrl())
                .position(banner.getPosition().name())
                .displayOrder(banner.getDisplayOrder())
                .isActive(banner.isActive())
                .createdAt(banner.getCreatedAt())
                .updatedAt(banner.getUpdatedAt())
                .build();
    }
}