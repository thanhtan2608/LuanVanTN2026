package org.example.lv_be.module.catalog.domain.entity;

import lombok.*;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceItem {
    private Long id;              // bigint(20) AUTO_INCREMENT
    private Long categoryId;      // bigint(20) -> Cột category_id
    private String name;          // varchar(255)
    private double price;         // decimal(10,2)
    private int durationMinutes;  // int(11) -> Cột duration_minutes
    private String imageUrl;      // varchar(500) -> Cột image_url
    private boolean deleted;      // tinyint(1) -> Cột is_deleted
    private boolean active;       // tinyint(1) -> Cột is_active

    public void validateSelf() {
        if (this.name == null || this.name.isBlank()) {
            throw new CatalogDomainException("Tên dịch vụ kỹ thuật không được để trống!");
        }
        if (this.price < 0) {
            throw new CatalogDomainException("Giá dịch vụ không được âm!");
        }
        if (this.durationMinutes <= 0) {
            throw new CatalogDomainException("Thời gian thực hiện dịch vụ phải lớn hơn 0 phút!");
        }
    }

    public void updatePrice(double newPrice) {
        if (newPrice < 0) {
            throw new CatalogDomainException("Không thể cập nhật giá dịch vụ thành số âm!");
        }
        this.price = newPrice;
    }

    public void softDelete() {
        this.deleted = true;
        this.active = false;
    }
}