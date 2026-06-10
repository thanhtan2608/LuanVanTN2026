package org.example.lv_be.module.catalog.application.dto.category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.lv_be.common.enums.ItemType;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateCategoryRequest {
    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;
    private String description;
    @NotNull(message = "Loại danh mục (SERVICE/PRODUCT) không được trống")
    private ItemType type;
}