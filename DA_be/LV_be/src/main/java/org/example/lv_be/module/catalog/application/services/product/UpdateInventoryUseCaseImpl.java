package org.example.lv_be.module.catalog.application.services.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.interfaces.product.IUpdateInventoryUseCase;
import org.example.lv_be.module.catalog.domain.entity.Product;
import org.example.lv_be.module.catalog.domain.repository.IProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateInventoryUseCaseImpl implements IUpdateInventoryUseCase {

    private final IProductRepository productRepository;

    @Override
    @Transactional // Đảm bảo an toàn dữ liệu, nếu lỗi sẽ rollback lại
    public void execute(Long productId, int quantityChange) {
        // 1. Tìm sản phẩm trong DB
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm có ID: " + productId));

        // 2. Kích hoạt logic Domain để tính toán tồn kho mới
        product.adjustStock(quantityChange);

        // 3. Lưu lại xuống Database
        productRepository.sourceSave(product);
    }
}
