package org.example.lv_be.module.catalog.application.services.product;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.dto.product.ProductResponse;
import org.example.lv_be.module.catalog.application.interfaces.product.IGetProductByIdUseCase;
import org.example.lv_be.module.catalog.application.mappers.ProductMapper;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import org.example.lv_be.module.catalog.domain.repository.IProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProductByIdUseCaseImpl implements IGetProductByIdUseCase {

    private final IProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse execute(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new CatalogDomainException("Sản phẩm sáp/gel yêu cầu không tồn tại trên hệ thống!"));
    }
}