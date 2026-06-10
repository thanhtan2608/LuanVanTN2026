package org.example.lv_be.module.catalog.infrastructure.persistence.mapper;

import org.example.lv_be.module.catalog.domain.entity.Product;
import org.example.lv_be.module.catalog.infrastructure.persistence.entity.ProductJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {
    ProductJpaEntity toJpaEntity(Product domain);
    Product toDomainEntity(ProductJpaEntity jpa);
}