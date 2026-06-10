package org.example.lv_be.module.catalog.application.services.product;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.interfaces.product.IDeleteProductUseCase;
import org.example.lv_be.module.catalog.domain.entity.Product;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import org.example.lv_be.module.catalog.domain.repository.IProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCaseImpl implements IDeleteProductUseCase {

    private final IProductRepository productRepository;

    @Override
    @Transactional
    public void execute(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CatalogDomainException("Không tìm thấy sản phẩm yêu cầu xóa mềm!"));

        product.softDelete(); // Đóng băng tồn kho về 0 và hủy kích hoạt
        productRepository.sourceSave(product);
    }
}