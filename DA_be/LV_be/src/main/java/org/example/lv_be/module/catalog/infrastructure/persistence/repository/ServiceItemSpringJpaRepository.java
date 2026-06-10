package org.example.lv_be.module.catalog.infrastructure.persistence.repository;

import org.example.lv_be.module.catalog.infrastructure.persistence.entity.ServiceItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceItemSpringJpaRepository extends JpaRepository<ServiceItemJpaEntity, Long> {
    List<ServiceItemJpaEntity> findByCategoryIdAndActiveTrueAndDeletedFalse(Long categoryId);
    List<ServiceItemJpaEntity> findByActiveTrueAndDeletedFalse();
    boolean existsByName(String name);
}