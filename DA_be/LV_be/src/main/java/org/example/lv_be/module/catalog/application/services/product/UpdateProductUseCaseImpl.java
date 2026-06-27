package org.example.lv_be.module.catalog.application.services.product;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.dto.product.UpdateProductRequest;
import org.example.lv_be.module.catalog.application.dto.product.ProductResponse;
import org.example.lv_be.module.catalog.application.interfaces.product.IUpdateProductUseCase;
import org.example.lv_be.module.catalog.application.mappers.ProductMapper;
import org.example.lv_be.module.catalog.domain.entity.Product;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import org.example.lv_be.module.catalog.domain.repository.ICategoryRepository;
import org.example.lv_be.module.catalog.domain.repository.IProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.example.lv_be.core.storage.ICloudStorageService;

@Service
@RequiredArgsConstructor
public class UpdateProductUseCaseImpl implements IUpdateProductUseCase {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ICloudStorageService cloudStorageService;

    @Override
    @Transactional
    public ProductResponse execute(Long id, UpdateProductRequest request, MultipartFile imageFile) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CatalogDomainException("Không tìm thấy thông tin sản phẩm cần sửa!"));

        categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CatalogDomainException("Danh mục mới chọn không hợp lệ!"));

        product.setCategoryId(request.getCategoryId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setActive(request.isActive());

        if (imageFile != null && !imageFile.isEmpty()) {
            String newUrl = cloudStorageService.uploadFile(imageFile, "products");
            product.setImageUrl(newUrl);
        }

        product.validateSelf();
        Product updatedProduct = productRepository.sourceSave(product);
        return productMapper.toResponse(updatedProduct);
    }
}