package org.example.lv_be.module.catalog.application.services.category;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.interfaces.category.IDeleteCategoryUseCase;
import org.example.lv_be.module.catalog.domain.entity.Category;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import org.example.lv_be.module.catalog.domain.repository.ICategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCaseImpl implements IDeleteCategoryUseCase {

    private final ICategoryRepository categoryRepository;

    @Override
    @Transactional
    public void execute(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CatalogDomainException("Không tìm thấy danh mục yêu cầu xóa!"));

        category.softDelete(); // Gọi logic nghiệp vụ xóa mềm ẩn khỏi UI
        categoryRepository.sourceSave(category);
    }
}