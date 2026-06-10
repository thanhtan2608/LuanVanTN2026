package org.example.lv_be.module.catalog.domain.repository;

import org.example.lv_be.module.catalog.domain.entity.Product;
import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    Optional<Product> findById(Long id);
    List<Product> findByCategoryIdAndBranchId(Long categoryId, Long branchId); // Lọc kho theo danh mục và chi nhánh cửa hàng
    List<Product> findLowStockProducts(Long branchId, int threshold); // Tìm sản phẩm cạn kho theo từng chi nhánh salon
    List<Product> searchByName(String name);
    Product sourceSave(Product product);
}