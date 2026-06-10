package org.example.lv_be.module.catalog.application.dto.category;
import lombok.*;
import org.example.lv_be.common.enums.ItemType;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private ItemType type;
    private LocalDateTime createdAt;
    private boolean active;
}