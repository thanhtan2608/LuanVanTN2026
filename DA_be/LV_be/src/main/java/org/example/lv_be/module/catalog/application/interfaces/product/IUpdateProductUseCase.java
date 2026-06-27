package org.example.lv_be.module.catalog.application.interfaces.product;

import org.example.lv_be.module.catalog.application.dto.product.ProductResponse;
import org.example.lv_be.module.catalog.application.dto.product.UpdateProductRequest;
import org.springframework.web.multipart.MultipartFile;

public interface IUpdateProductUseCase {
    ProductResponse execute(Long id, UpdateProductRequest request, MultipartFile imageFile);
}
