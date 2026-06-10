package org.example.lv_be.module.catalog.application.interfaces.category;

import org.example.lv_be.module.catalog.application.dto.category.CategoryResponse;
import org.example.lv_be.module.catalog.application.dto.category.CreateCategoryRequest;

public interface ICreateCategoryUseCase { CategoryResponse execute(CreateCategoryRequest request); }
