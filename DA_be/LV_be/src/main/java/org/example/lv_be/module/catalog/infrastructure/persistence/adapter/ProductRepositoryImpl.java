package org.example.lv_be.module.catalog.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.domain.entity.Product;
import org.example.lv_be.module.catalog.domain.repository.IProductRepository;
import org.example.lv_be.module.catalog.infrastructure.persistence.entity.ProductJpaEntity;
import org.example.lv_be.module.catalog.infrastructure.persistence.mapper.ProductPersistenceMapper;
import org.example.lv_be.module.catalog.infrastructure.persistence.repository.ProductSpringJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements IProductRepository {

    private final ProductSpringJpaRepository jpaRepository;
    private final ProductPersistenceMapper mapper;

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
                .filter(jpa -> !jpa.isDeleted())
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Product> findByCategoryIdAndBranchId(Long categoryId, Long branchId) {
        return jpaRepository.findByCategoryIdAndBranchIdAndDeletedFalse(categoryId, branchId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<Product> findLowStockProducts(Long branchId, int threshold) {
        return jpaRepository.findLowStockProducts(branchId, threshold).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<Product> searchByName(String name) {
        return jpaRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public Product sourceSave(Product product) {
        ProductJpaEntity jpa = mapper.toJpaEntity(product);
        ProductJpaEntity saved = jpaRepository.save(jpa);
        return mapper.toDomainEntity(saved);
    }
}