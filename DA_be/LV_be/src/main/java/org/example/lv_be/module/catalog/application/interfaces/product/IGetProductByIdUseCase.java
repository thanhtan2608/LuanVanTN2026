package org.example.lv_be.module.catalog.application.interfaces.product;

import org.example.lv_be.module.catalog.application.dto.product.ProductResponse;

public interface IGetProductByIdUseCase { ProductResponse execute(Long id); }
