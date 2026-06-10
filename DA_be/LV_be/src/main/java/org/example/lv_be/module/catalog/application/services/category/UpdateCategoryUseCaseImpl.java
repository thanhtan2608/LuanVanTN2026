package org.example.lv_be.module.catalog.application.services.category;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.dto.category.UpdateCategoryRequest;
import org.example.lv_be.module.catalog.application.dto.category.CategoryResponse;
import org.example.lv_be.module.catalog.application.interfaces.category.IUpdateCategoryUseCase;
import org.example.lv_be.module.catalog.application.mappers.CategoryMapper;
import org.example.lv_be.module.catalog.domain.entity.Category;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import org.example.lv_be.module.catalog.domain.repository.ICategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCategoryUseCaseImpl implements IUpdateCategoryUseCase {

    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponse execute(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CatalogDomainException("Không tìm thấy danh mục yêu cầu chỉnh sửa!"));

        category.setName(request.getName());
        category.setActive(request.isActive());

        category.validateSelf();

        Category updatedCategory = categoryRepository.sourceSave(category);
        return categoryMapper.toResponse(updatedCategory);
    }
}