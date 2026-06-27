package org.example.lv_be.module.catalog.application.dto.product;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateProductRequest {
    @NotNull(message = "Mã danh mục không được trống")
    private Long categoryId;
    @NotBlank(message = "Tên sản phẩm không được trống")
    private String name;
    @Min(value = 0, message = "Giá bán không được âm")
    private double price;
    private boolean active;
    private String imageUrl;
}