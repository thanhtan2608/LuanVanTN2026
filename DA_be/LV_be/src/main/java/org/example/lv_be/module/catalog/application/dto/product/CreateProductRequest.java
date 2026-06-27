package org.example.lv_be.module.catalog.application.dto.product;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateProductRequest {
    @NotNull(message = "Mã danh mục không được trống")
    private Long categoryId;
    @NotNull(message = "Mã chi nhánh không được trống")
    private Long branchId;
    @NotBlank(message = "Tên sản phẩm không được trống")
    private String name;
    @Min(value = 0, message = "Giá bán không được âm")
    private double price;
    @Min(value = 0, message = "Số lượng nhập kho ban đầu không được âm")
    private int stockQuantity;
    private String imageUrl;
}