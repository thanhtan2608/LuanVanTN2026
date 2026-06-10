package org.example.lv_be.module.catalog.application.mappers;
import org.example.lv_be.module.catalog.application.dto.product.*;
import org.example.lv_be.module.catalog.domain.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toDomain(CreateProductRequest request);
    ProductResponse toResponse(Product domain);
}