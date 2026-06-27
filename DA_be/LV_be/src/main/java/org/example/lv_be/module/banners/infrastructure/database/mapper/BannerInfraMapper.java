package org.example.lv_be.module.banners.infrastructure.database.mapper;

import org.example.lv_be.module.banners.domain.entity.Banner;
import org.example.lv_be.module.banners.infrastructure.database.entity.BannerJpaEntity;

public class BannerInfraMapper {

    // Chuyển từ JPA (DB) lên Domain (Lõi)
    public static Banner toDomain(BannerJpaEntity entity) {
        if (entity == null) return null;

        return Banner.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .imageUrl(entity.getImageUrl())
                .targetUrl(entity.getTargetUrl())
                .position(entity.getPosition())
                .displayOrder(entity.getDisplayOrder())
                .isActive(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    // Chuyển từ Domain (Lõi) xuống JPA (DB) để lưu
    public static BannerJpaEntity toJpaEntity(Banner domain) {
        if (domain == null) return null;

        return BannerJpaEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .imageUrl(domain.getImageUrl())
                .targetUrl(domain.getTargetUrl())
                .position(domain.getPosition())
                .displayOrder(domain.getDisplayOrder())
                .isActive(domain.isActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}