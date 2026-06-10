package org.example.lv_be.module.catalog.domain.entity;

import lombok.*;
import org.example.lv_be.common.enums.ItemType;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    private Long id;          // bigint(20) AUTO_INCREMENT
    private String name;      // varchar(255)
    private ItemType type;    // enum('SERVICE','PRODUCT') từ common.enums
    private LocalDateTime createdAt; // timestamp DEFAULT CURRENT_TIMESTAMP
    private boolean deleted;  // tinyint(1) -> Ánh xạ từ cột is_deleted
    private boolean active;   // tinyint(1) -> Ánh xạ từ cột is_active

    public void validateSelf() {
        if (this.name == null || this.name.isBlank()) {
            throw new CatalogDomainException("Tên danh mục không được để trống!");
        }
        if (this.type == null) {
            throw new CatalogDomainException("Loại danh mục (SERVICE/PRODUCT) không hợp lệ!");
        }
    }

    public void softDelete() {
        this.deleted = true;
        this.active = false;
    }
}