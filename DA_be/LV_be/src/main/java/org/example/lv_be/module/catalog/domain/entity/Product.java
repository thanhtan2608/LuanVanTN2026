package org.example.lv_be.module.catalog.domain.entity;

import lombok.*;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private Long id;             // bigint(20) AUTO_INCREMENT
    private Long categoryId;     // bigint(20) -> Cột category_id
    private Long branchId;       // bigint(20) -> Cột branch_id (Quản lý kho theo chi nhánh)
    private String name;         // varchar(255)
    private double price;        // decimal(10,2)
    private int stockQuantity;   // int(11) -> Cột stock_quantity khớp 100% SQL
    private boolean deleted;     // tinyint(1) -> Cột is_deleted
    private boolean active;      // tinyint(1) -> Cột is_active

    public void validateSelf() {
        if (this.name == null || this.name.isBlank()) {
            throw new CatalogDomainException("Tên sản phẩm sáp/gel không được để trống!");
        }
        if (this.price < 0) {
            throw new CatalogDomainException("Giá bán sản phẩm không được số âm!");
        }
        if (this.stockQuantity < 0) {
            throw new CatalogDomainException("Số lượng hàng tồn kho không được nhỏ hơn 0!");
        }
    }

    /**
     * Nghiệp vụ bán hàng/xuất kho tại salon chi nhánh
     */
    public void reduceStock(int quantity) {
        if (this.deleted) {
            throw new CatalogDomainException("Sản phẩm đã bị xóa khỏi hệ thống kinh doanh!");
        }
        if (!this.active) {
            throw new CatalogDomainException("Sản phẩm đang tạm dừng ngưng bán!");
        }
        if (quantity <= 0) {
            throw new CatalogDomainException("Số lượng mua hàng phải lớn hơn 0!");
        }
        if (this.stockQuantity < quantity) {
            throw new CatalogDomainException("Hàng trong kho chi nhánh không đủ cung ứng! Hiện còn: " + this.stockQuantity);
        }
        this.stockQuantity -= quantity;
    }

    /**
     * Nghiệp vụ nhập kho bổ sung hàng hóa cho chi nhánh
     */
    public void replenishStock(int quantity) {
        if (this.deleted) {
            throw new CatalogDomainException("Không thể nhập kho cho sản phẩm đã bị xóa vĩnh viễn!");
        }
        if (quantity <= 0) {
            throw new CatalogDomainException("Số lượng nhập kho bổ sung phải lớn hơn 0!");
        }
        this.stockQuantity += quantity;
    }
    // 🧠 LOGIC NGHIỆP VỤ: Tăng / Giảm số lượng tồn kho an toàn
    public void adjustStock(int quantityChange) {
        // Nếu quantityChange là số âm (bán hàng), kiểm tra xem kho có đủ đồ không
        if (quantityChange < 0 && (this.stockQuantity + quantityChange < 0)) {
            throw new RuntimeException("Tồn kho không đủ! Sản phẩm '" + this.name + "' chỉ còn " + this.stockQuantity + " hộp.");
        }

        this.stockQuantity += quantityChange;
    }

    public void softDelete() {
        this.deleted = true;
        this.active = false;
        this.stockQuantity = 0; // Xóa sản phẩm thì đóng băng kho về 0
    }
}