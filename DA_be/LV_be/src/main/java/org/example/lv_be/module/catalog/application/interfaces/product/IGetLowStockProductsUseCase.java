package org.example.lv_be.module.catalog.application.interfaces.product;

import org.example.lv_be.module.catalog.application.dto.product.ProductResponse;

import java.util.List;

public interface IGetLowStockProductsUseCase { List<ProductResponse> execute(Long branchId, int threshold); }
