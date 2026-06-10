package org.example.lv_be.module.catalog.presentation.rest.category;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.ItemType;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.catalog.application.dto.category.CategoryResponse;
import org.example.lv_be.module.catalog.application.interfaces.category.IGetActiveCategoriesUseCase;
import org.example.lv_be.module.catalog.application.interfaces.category.IGetCategoryByIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * =========================================================================
 * 🌍 LƯU Ý QUAN TRỌNG CHO FRONTEND (PUBLIC CATEGORIES)
 * =========================================================================
 * Các API này phục vụ cho trang chủ, màn hình tìm kiếm, bộ lọc Dropdown.
 * 🔹 KHÔNG YÊU CẦU ĐĂNG NHẬP (Bỏ trống Header Authorization).
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/public/categories")
@RequiredArgsConstructor
public class PublicCategoryController {

    private final IGetActiveCategoriesUseCase getActiveCategoriesUseCase;
    private final IGetCategoryByIdUseCase getCategoryByIdUseCase;

    /**
     * 📘 FRONTEND NOTE: LẤY DANH SÁCH DANH MỤC THEO LOẠI DỊCH VỤ HOẶC SẢN PHẨM
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/categories?type=SERVICE
     * 🔹 PARAMETERS: type (Truyền giá trị chữ hoa bắt buộc: SERVICE hoặc PRODUCT)
     * 🔹 RESPONSE EX: Trả về mảng danh mục sạch (chưa bị xóa, đang active)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getActiveCategories(@RequestParam ItemType type) {
        List<CategoryResponse> data = getActiveCategoriesUseCase.execute(type);
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy danh sách danh mục thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XEM CHI TIẾT 1 DANH MỤC THEO ID
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/categories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        CategoryResponse data = getCategoryByIdUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy chi tiết danh mục thành công!"));
    }
}