package org.example.lv_be.module.banners.infrastructure.database.repository;

import org.example.lv_be.module.banners.domain.entity.BannerPosition;
import org.example.lv_be.module.banners.infrastructure.database.entity.BannerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerSpringJpaRepository extends JpaRepository<BannerJpaEntity, Long> {

    // Ma thuật của Spring Data JPA:
    // Dịch ra SQL: SELECT * FROM banners WHERE position = ? AND is_active = true ORDER BY display_order ASC
    List<BannerJpaEntity> findByPositionAndIsActiveTrueOrderByDisplayOrderAsc(BannerPosition position);

}