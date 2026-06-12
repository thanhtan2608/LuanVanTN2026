package org.example.lv_be.module.catalog.application.interfaces.product;

public interface IUpdateInventoryUseCase {
    /**
     * @param productId ID của sản phẩm cần cập nhật
     * @param quantityChange Số lượng thay đổi (Số dương = Nhập kho, Số âm = Xuất kho/Bán)
     */
    void execute(Long productId, int quantityChange);
}
