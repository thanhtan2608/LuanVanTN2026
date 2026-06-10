package org.example.lv_be.module.catalog.application.services.category;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.dto.category.CategoryResponse;
import org.example.lv_be.module.catalog.application.interfaces.category.IGetCategoryByIdUseCase;
import org.example.lv_be.module.catalog.application.mappers.CategoryMapper;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import org.example.lv_be.module.catalog.domain.repository.ICategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCategoryByIdUseCaseImpl implements IGetCategoryByIdUseCase {

    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse execute(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new CatalogDomainException("Danh mục không tồn tại trên hệ thống!"));
    }
}