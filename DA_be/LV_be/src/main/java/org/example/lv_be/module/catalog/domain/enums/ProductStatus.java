package org.example.lv_be.module.catalog.domain.enums;

/**
 * Trạng thái thương mại và vận hành kho của sản phẩm
 */
public enum ProductStatus {
    IN_STOCK,       // Còn hàng trong kho, sẵn sàng bán
    OUT_OF_STOCK,   // Hết hàng tạm thời
    DISCONTINUED    // Ngừng kinh doanh vĩnh viễn (Không cho phép nhập kho hay bán nữa)
}