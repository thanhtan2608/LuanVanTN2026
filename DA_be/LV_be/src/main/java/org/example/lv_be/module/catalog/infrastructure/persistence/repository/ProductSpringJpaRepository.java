package org.example.lv_be.module.catalog.infrastructure.persistence.repository;

import org.example.lv_be.module.catalog.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductSpringJpaRepository extends JpaRepository<ProductJpaEntity, Long> {
    List<ProductJpaEntity> findByCategoryIdAndBranchIdAndDeletedFalse(Long categoryId, Long branchId);
    List<ProductJpaEntity> findByNameContainingIgnoreCaseAndDeletedFalse(String name);

    @Query("SELECT p FROM ProductJpaEntity p WHERE p.branchId = :branchId AND p.stockQuantity <= :threshold AND p.deleted = false")
    List<ProductJpaEntity> findLowStockProducts(@Param("branchId") Long branchId, @Param("threshold") int threshold);
}