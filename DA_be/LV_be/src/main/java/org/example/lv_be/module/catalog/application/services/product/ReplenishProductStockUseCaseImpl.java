package org.example.lv_be.module.catalog.application.services.product;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.dto.product.ProductResponse;
import org.example.lv_be.module.catalog.application.interfaces.product.IReplenishProductStockUseCase;
import org.example.lv_be.module.catalog.application.mappers.ProductMapper;
import org.example.lv_be.module.catalog.domain.entity.Product;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import org.example.lv_be.module.catalog.domain.repository.IProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplenishProductStockUseCaseImpl implements IReplenishProductStockUseCase {

    private final IProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse execute(Long id, int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CatalogDomainException("Không tìm thấy sản phẩm để tiến hành nhập kho!"));

        // Gọi hàm xử lý cốt lõi của Rich Domain Model để tự động tính toán trạng thái kho hàng
        product.replenishStock(quantity);

        Product updatedProduct = productRepository.sourceSave(product);
        return productMapper.toResponse(updatedProduct);
    }
}