package org.example.lv_be.module.catalog.application.mappers;
import org.example.lv_be.module.catalog.application.dto.category.*;
import org.example.lv_be.module.catalog.domain.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toDomain(CreateCategoryRequest request);
    CategoryResponse toResponse(Category domain);
}