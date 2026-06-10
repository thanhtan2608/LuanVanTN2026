package org.example.lv_be.module.catalog.application.interfaces.category;

import org.example.lv_be.module.catalog.application.dto.category.CategoryResponse;

public interface IGetCategoryByIdUseCase { CategoryResponse execute(Long id); }
