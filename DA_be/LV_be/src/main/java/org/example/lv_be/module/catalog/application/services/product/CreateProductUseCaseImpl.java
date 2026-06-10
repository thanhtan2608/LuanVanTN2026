package org.example.lv_be.module.catalog.application.services.product;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.dto.product.CreateProductRequest;
import org.example.lv_be.module.catalog.application.dto.product.ProductResponse;
import org.example.lv_be.module.catalog.application.interfaces.product.ICreateProductUseCase;
import org.example.lv_be.module.catalog.application.mappers.ProductMapper;
import org.example.lv_be.module.catalog.domain.entity.Product;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import org.example.lv_be.module.catalog.domain.repository.ICategoryRepository;
import org.example.lv_be.module.catalog.domain.repository.IProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProductUseCaseImpl implements ICreateProductUseCase {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse execute(CreateProductRequest request) {
        categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CatalogDomainException("Danh mục sản phẩm chọn không tồn tại!"));

        Product product = productMapper.toDomain(request);
        product.setActive(true);
        product.setDeleted(false);

        product.validateSelf();

        Product savedProduct = productRepository.sourceSave(product);
        return productMapper.toResponse(savedProduct);
    }
}