package org.example.lv_be.module.catalog.application.dto.category;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateCategoryRequest {
    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;
    private String description;
    private boolean active;
}