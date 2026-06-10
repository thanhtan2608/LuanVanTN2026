package org.example.lv_be.module.catalog.presentation.rest.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.catalog.application.dto.product.CreateProductRequest;
import org.example.lv_be.module.catalog.application.dto.product.ProductResponse;
import org.example.lv_be.module.catalog.application.dto.product.ReplenishStockRequest;
import org.example.lv_be.module.catalog.application.dto.product.UpdateProductRequest;
import org.example.lv_be.module.catalog.application.interfaces.product.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * =========================================================================
 * 🔐 LƯU Ý CHO FRONTEND VỀ PHÂN QUYỀN KHO HÀNG (ROLES SECURITY)
 * =========================================================================
 * Cổng này mở rộng cho cả tài khoản ADMIN hệ thống và Quản lý salon (MANAGER)
 * ở các chi nhánh gọi để nhập thêm hàng hóa sáp vuốt, gel tạo kiểu.
 * Header: Authorization = Bearer [Token]
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class AdminProductController {

    private final ICreateProductUseCase createProductUseCase;
    private final IUpdateProductUseCase updateProductUseCase;
    private final IReplenishProductStockUseCase replenishProductStockUseCase;
    private final IGetLowStockProductsUseCase getLowStockProductsUseCase;
    private final IDeleteProductUseCase deleteProductUseCase;

    /**
     * 📘 FRONTEND NOTE: ĐĂNG KÝ SẢN PHẨM MỚI VÀO KHO CHI NHÁNH
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/admin/products
     * 🔹 REQUEST BODY (JSON):
     * {
     * "categoryId": 2,
     * "branchId": 1, // Gắn trực tiếp sản phẩm thuộc về kho của tiệm Salon cơ sở 1
     * "name": "Sáp Vuốt Tóc Volcanic Clay V5",
     * "price": 340000,
     * "stockQuantity": 50 // Số lượng hàng tồn kho ban đầu khi mở tiệm
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse data = createProductUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Khai báo sản phẩm mới vào kho thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: CHỈNH SỬA THÔNG TIN / GIÁ BÁN SẢN PHẨM
     * ------------------------------------------------------------------------
     * 🔹 METHOD: PUT
     * 🔹 URL CHUẨN: /api/v1/admin/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        ProductResponse data = updateProductUseCase.execute(id, request);
        return ResponseEntity.ok(ApiResponse.success(data, "Cập nhật dữ liệu sản phẩm thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: NHẬP LÔ HÀNG BỔ SUNG (CỘNG DỒN SỐ LƯỢNG VÀO KHO)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/admin/products/{id}/replenish (Ví dụ: /api/v1/admin/products/5/replenish)
     * 🔹 REQUEST BODY (JSON):
     * {
     * "quantity": 20 // Số lượng chai sáp mới lấy về từ nhà phân phối để cộng dồn kho
     * }
     * 🔹 CƠ CHẾ NGẦM: Nếu sản phẩm đang ở trạng thái OUT_OF_STOCK, lệnh nhập kho > 0
     * này sẽ tự động kích hoạt chuyển trạng thái thành IN_STOCK ngay lập tức.
     */
    @PostMapping("/{id}/replenish")
    public ResponseEntity<ApiResponse<ProductResponse>> replenishStock(
            @PathVariable Long id,
            @Valid @RequestBody ReplenishStockRequest request) {
        ProductResponse data = replenishProductStockUseCase.execute(id, request.getQuantity());
        return ResponseEntity.ok(ApiResponse.success(data, "Nhập thêm hàng bổ sung vào kho chi nhánh thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: QUÉT DANH SÁCH CÁC SẢN PHẨM SẮP HẾT HÀNG TRÊN KỆ ĐỂ CẢNH BÁO
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/admin/products/low-stock?branchId=1&threshold=5
     * 🔹 QUERY PARAMS:
     * - branchId: ID chi nhánh salon cần kiểm toán kho
     * - threshold: Mốc cảnh báo cạn hàng (Ví dụ: truyền vào số 5, hệ thống trả về toàn bộ sản phẩm có tồn kho <= 5)
     */
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getLowStockProducts(
            @RequestParam Long branchId,
            @RequestParam int threshold) {
        List<ProductResponse> data = getLowStockProductsUseCase.execute(branchId, threshold);
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy danh sách sản phẩm sắp cạn kho thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XÓA SẢN PHẨM KHỎI KỆ HÀNG
     * ------------------------------------------------------------------------
     * 🔹 METHOD: DELETE
     * 🔹 URL CHUẨN: /api/v1/admin/products/{id}
     * 🔹 HÀNH VI NGẦM: Chuyển is_deleted = 1, đóng băng số lượng tồn kho stock_quantity = 0.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        deleteProductUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa sản phẩm khỏi kệ hàng thành công!"));
    }
}