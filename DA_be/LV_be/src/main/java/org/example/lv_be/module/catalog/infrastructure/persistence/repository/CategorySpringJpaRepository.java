package org.example.lv_be.module.catalog.infrastructure.persistence.repository;

import org.example.lv_be.common.enums.ItemType;
import org.example.lv_be.module.catalog.infrastructure.persistence.entity.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategorySpringJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {
    List<CategoryJpaEntity> findByTypeAndActiveTrueAndDeletedFalse(ItemType type);
    boolean existsByNameAndType(String name, ItemType type);
}