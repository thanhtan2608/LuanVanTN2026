package org.example.lv_be.module.catalog.application.dto.product;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductResponse {
    private Long id;
    private Long categoryId;
    private Long branchId;
    private String name;
    private double price;
    private int stockQuantity;
    private boolean active;
}