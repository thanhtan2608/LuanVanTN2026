package org.example.lv_be.module.catalog.application.interfaces.product;

import org.example.lv_be.module.catalog.application.dto.product.ProductResponse;

import java.util.List;

public interface IGetProductsByBranchUseCase { List<ProductResponse> execute(Long categoryId, Long branchId); }
