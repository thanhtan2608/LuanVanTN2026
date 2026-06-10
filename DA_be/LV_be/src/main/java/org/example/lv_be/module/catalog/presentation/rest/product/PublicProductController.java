package org.example.lv_be.module.catalog.presentation.rest.product;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.catalog.application.dto.product.ProductResponse;
import org.example.lv_be.module.catalog.application.interfaces.product.IGetProductByIdUseCase;
import org.example.lv_be.module.catalog.application.interfaces.product.IGetProductsByBranchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * =========================================================================
 * 🌍 LƯU Ý KINH DOANH CHUỖI CỬA HÀNG (MULTI-BRANCH CATALOG PUBLIC)
 * =========================================================================
 * API xem sản phẩm công khai yêu cầu truyền mã 'branchId'.
 * Khách hàng đang ở chi nhánh nào, App sẽ hiển thị chính xác các mặt hàng sáp/gel
 * kèm số lượng tồn kho thực tế đang bày bán tại cơ sở đó. Không yêu cầu đăng nhập.
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/public/products")
@RequiredArgsConstructor
public class PublicProductController {

    private final IGetProductsByBranchUseCase getProductsByBranchUseCase;
    private final IGetProductByIdUseCase getProductByIdUseCase;

    /**
     * 📘 FRONTEND NOTE: XEM DANH SÁCH SẢN PHẨM THEO CHI NHÁNH VÀ THEO DANH MỤC
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/products?branchId=1&categoryId=2
     * 🔹 QUERY PARAMS (Bắt buộc):
     * - branchId: Xem kho của tiệm nào (Ví dụ: Salon Quận 1)
     * - categoryId: Xem theo phân loại nào (Ví dụ: Nhóm Sáp vuốt tóc)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByBranch(
            @RequestParam Long categoryId,
            @RequestParam Long branchId) {
        List<ProductResponse> data = getProductsByBranchUseCase.execute(categoryId, branchId);
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy danh sách mặt hàng tại chi nhánh thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XEM CHI TIẾT GIÁ BÁN VÀ TỒN KHO CỦA 1 SẢN PHẨM CỤ THỂ
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse data = getProductByIdUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy thông tin chi tiết sản phẩm thành công!"));
    }
}