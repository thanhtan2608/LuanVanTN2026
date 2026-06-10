package org.example.lv_be.module.catalog.application.services.product;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.dto.product.ProductResponse;
import org.example.lv_be.module.catalog.application.interfaces.product.IGetProductsByBranchUseCase;
import org.example.lv_be.module.catalog.application.mappers.ProductMapper;
import org.example.lv_be.module.catalog.domain.repository.IProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProductsByBranchUseCaseImpl implements IGetProductsByBranchUseCase {

    private final IProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> execute(Long categoryId, Long branchId) {
        return productRepository.findByCategoryIdAndBranchId(categoryId, branchId).stream()
                .map(productMapper::toResponse)
                .toList();
    }
}