package org.example.lv_be.module.hairstyles.infrastructure.persistence.repository;

import org.example.lv_be.common.enums.FaceShape;
import org.example.lv_be.module.hairstyles.infrastructure.persistence.entity.HairstyleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HairstyleSpringJpaRepository extends JpaRepository<HairstyleJpaEntity, Long> {
    List<HairstyleJpaEntity> findByFaceShapeAndActiveTrueAndDeletedFalse(FaceShape faceShape);
    List<HairstyleJpaEntity> findByActiveTrueAndDeletedFalse();
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}