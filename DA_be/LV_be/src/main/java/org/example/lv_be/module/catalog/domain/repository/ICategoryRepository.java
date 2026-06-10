package org.example.lv_be.module.catalog.domain.repository;

import org.example.lv_be.common.enums.ItemType;
import org.example.lv_be.module.catalog.domain.entity.Category;
import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {
    Optional<Category> findById(Long id);
    List<Category> findAllActiveByType(ItemType type); // Tìm danh mục không bị xóa mềm (deleted = false)
    boolean existsByNameAndType(String name, ItemType type);
    Category sourceSave(Category category);
}