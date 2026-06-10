package org.example.lv_be.module.catalog.infrastructure.persistence.mapper;

import org.example.lv_be.module.catalog.domain.entity.Category;
import org.example.lv_be.module.catalog.infrastructure.persistence.entity.CategoryJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryPersistenceMapper {
    CategoryJpaEntity toJpaEntity(Category domain);
    Category toDomainEntity(CategoryJpaEntity jpa);
}