package org.example.lv_be.module.catalog.application.services.category;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.dto.category.CreateCategoryRequest;
import org.example.lv_be.module.catalog.application.dto.category.CategoryResponse;
import org.example.lv_be.module.catalog.application.interfaces.category.ICreateCategoryUseCase;
import org.example.lv_be.module.catalog.application.mappers.CategoryMapper;
import org.example.lv_be.module.catalog.domain.entity.Category;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import org.example.lv_be.module.catalog.domain.repository.ICategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCaseImpl implements ICreateCategoryUseCase {

    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponse execute(CreateCategoryRequest request) {
        if (categoryRepository.existsByNameAndType(request.getName(), request.getType())) {
            throw new CatalogDomainException("Tên danh mục này đã tồn tại trong hệ thống!");
        }

        Category category = categoryMapper.toDomain(request);
        category.setActive(true);
        category.setDeleted(false);
        category.setCreatedAt(LocalDateTime.now());

        category.validateSelf(); // Kích hoạt Rich Domain Validation

        Category savedCategory = categoryRepository.sourceSave(category);
        return categoryMapper.toResponse(savedCategory);
    }
}