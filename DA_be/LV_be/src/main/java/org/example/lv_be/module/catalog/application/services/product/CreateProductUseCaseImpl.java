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
import org.springframework.web.multipart.MultipartFile;
import org.example.lv_be.core.storage.ICloudStorageService;

@Service
@RequiredArgsConstructor
public class CreateProductUseCaseImpl implements ICreateProductUseCase {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ICloudStorageService cloudStorageService;

    @Override
    @Transactional
    public ProductResponse execute(CreateProductRequest request, MultipartFile imageFile) {
        categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CatalogDomainException("Danh mục sản phẩm chọn không tồn tại!"));

        Product product = productMapper.toDomain(request);
        product.setActive(true);
        product.setDeleted(false);

        if (imageFile != null && !imageFile.isEmpty()) {
            String uploadedUrl = cloudStorageService.uploadFile(imageFile, "products");
            product.setImageUrl(uploadedUrl);
        }

        product.validateSelf();

        Product savedProduct = productRepository.sourceSave(product);
        return productMapper.toResponse(savedProduct);
    }
}