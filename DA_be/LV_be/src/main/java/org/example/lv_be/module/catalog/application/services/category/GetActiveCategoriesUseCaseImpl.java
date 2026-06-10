package org.example.lv_be.module.catalog.application.services.category;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.ItemType;
import org.example.lv_be.module.catalog.application.dto.category.CategoryResponse;
import org.example.lv_be.module.catalog.application.interfaces.category.IGetActiveCategoriesUseCase;
import org.example.lv_be.module.catalog.application.mappers.CategoryMapper;
import org.example.lv_be.module.catalog.domain.repository.ICategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetActiveCategoriesUseCaseImpl implements IGetActiveCategoriesUseCase {

    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> execute(ItemType type) {
        return categoryRepository.findAllActiveByType(type).stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}