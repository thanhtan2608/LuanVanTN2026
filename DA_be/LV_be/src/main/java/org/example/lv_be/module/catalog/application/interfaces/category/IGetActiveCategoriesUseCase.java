package org.example.lv_be.module.catalog.application.interfaces.category;

import org.example.lv_be.common.enums.ItemType;
import org.example.lv_be.module.catalog.application.dto.category.CategoryResponse;

import java.util.List;

public interface IGetActiveCategoriesUseCase { List<CategoryResponse> execute(ItemType type); }
