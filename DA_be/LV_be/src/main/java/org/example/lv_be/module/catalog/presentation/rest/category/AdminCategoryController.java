package org.example.lv_be.module.catalog.presentation.rest.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.catalog.application.dto.category.CategoryResponse;
import org.example.lv_be.module.catalog.application.dto.category.CreateCategoryRequest;
import org.example.lv_be.module.catalog.application.dto.category.UpdateCategoryRequest;
import org.example.lv_be.module.catalog.application.interfaces.category.ICreateCategoryUseCase;
import org.example.lv_be.module.catalog.application.interfaces.category.IDeleteCategoryUseCase;
import org.example.lv_be.module.catalog.application.interfaces.category.IUpdateCategoryUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * =========================================================================
 * 🔐 LƯU Ý BẢO MẬT CHO FRONTEND (ADMIN CATEGORY SECURITY)
 * =========================================================================
 * Toàn bộ API trong Controller này chỉ dành cho Admin cấu hình hệ thống Salon.
 * Frontend bắt buộc đính kèm Header: Authorization = Bearer [Admin_Token]
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    private final ICreateCategoryUseCase createCategoryUseCase;
    private final IUpdateCategoryUseCase updateCategoryUseCase;
    private final IDeleteCategoryUseCase deleteCategoryUseCase;

    /**
     * 📘 FRONTEND NOTE: TẠO MỚI DANH MỤC (DỊCH VỤ HOẶC SẢN PHẨM)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/admin/categories
     * 🔹 HEADERS: Content-Type = application/json, Authorization = Bearer [Token]
     * 🔹 REQUEST BODY (JSON):
     * {
     * "name": "Combo Hóa Chất Cao Cấp",
     * "type": "SERVICE" // Chấp nhận 2 giá trị duy nhất: "SERVICE" hoặc "PRODUCT"
     * }
     * 🔹 RESPONSE EX (success):
     * {
     * "success": true,
     * "code": 200,
     * "message": "Tạo danh mục mới thành công!",
     * "data": { "id": 3, "name": "Combo Hóa Chất Cao Cấp", "type": "SERVICE", "active": true }
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse data = createCategoryUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Tạo danh mục mới thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: CẬP NHẬT TÊN HOẶC TRẠNG THÁI DANH MỤC
     * ------------------------------------------------------------------------
     * 🔹 METHOD: PUT
     * 🔹 URL CHUẨN: /api/v1/admin/categories/{id} (Ví dụ: /api/v1/admin/categories/3)
     * 🔹 HEADERS: Content-Type = application/json, Authorization = Bearer [Token]
     * 🔹 REQUEST BODY (JSON):
     * {
     * "name": "Combo Hóa Chất Đẳng Cấp VIP",
     * "active": false // Gửi false nếu muốn ẩn tạm thời danh mục này khỏi App khách
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        CategoryResponse data = updateCategoryUseCase.execute(id, request);
        return ResponseEntity.ok(ApiResponse.success(data, "Cập nhật dữ liệu danh mục thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XÓA MỀM DANH MỤC KHỎI HỆ THỐNG
     * ------------------------------------------------------------------------
     * 🔹 METHOD: DELETE
     * 🔹 URL CHUẨN: /api/v1/admin/categories/{id}
     * 🔹 HEADERS: Authorization = Bearer [Token]
     * 🔹 CƠ CHẾ: Đổi cờ 'is_deleted = 1' ngầm. Khi thành công, Frontend xóa item khỏi giao diện UI.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        deleteCategoryUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa danh mục thành công khỏi hệ thống!"));
    }
}