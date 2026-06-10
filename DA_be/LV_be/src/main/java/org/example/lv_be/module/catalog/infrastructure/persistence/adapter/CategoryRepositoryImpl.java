package org.example.lv_be.module.catalog.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.ItemType;
import org.example.lv_be.module.catalog.domain.entity.Category;
import org.example.lv_be.module.catalog.domain.repository.ICategoryRepository;
import org.example.lv_be.module.catalog.infrastructure.persistence.entity.CategoryJpaEntity;
import org.example.lv_be.module.catalog.infrastructure.persistence.mapper.CategoryPersistenceMapper;
import org.example.lv_be.module.catalog.infrastructure.persistence.repository.CategorySpringJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements ICategoryRepository {

    private final CategorySpringJpaRepository jpaRepository;
    private final CategoryPersistenceMapper mapper;

    @Override
    public Optional<Category> findById(Long id) {
        return jpaRepository.findById(id)
                .filter(jpa -> !jpa.isDeleted())
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Category> findAllActiveByType(ItemType type) {
        return jpaRepository.findByTypeAndActiveTrueAndDeletedFalse(type).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public boolean existsByNameAndType(String name, ItemType type) {
        return jpaRepository.existsByNameAndType(name, type);
    }

    @Override
    public Category sourceSave(Category category) {
        CategoryJpaEntity jpa = mapper.toJpaEntity(category);
        CategoryJpaEntity saved = jpaRepository.save(jpa);
        return mapper.toDomainEntity(saved);
    }
}