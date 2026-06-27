package org.example.lv_be.module.catalog.application.interfaces.product;

import org.example.lv_be.module.catalog.application.dto.product.CreateProductRequest;
import org.example.lv_be.module.catalog.application.dto.product.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ICreateProductUseCase {
    ProductResponse execute(CreateProductRequest request, MultipartFile imageFile);
}
