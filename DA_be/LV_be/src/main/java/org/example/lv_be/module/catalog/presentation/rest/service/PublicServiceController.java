package org.example.lv_be.module.catalog.presentation.rest.service;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.catalog.application.dto.service.ServiceResponse;
import org.example.lv_be.module.catalog.application.interfaces.service.IGetAllActiveServicesUseCase;
import org.example.lv_be.module.catalog.application.interfaces.service.IGetServiceByIdUseCase;
import org.example.lv_be.module.catalog.application.interfaces.service.IGetServicesByCategoryUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * =========================================================================
 * 🌍 LƯU Ý CHO FRONTEND LUỒNG ĐẶT LỊCH (BOOKING FLOW)
 * =========================================================================
 * API hiển thị công khai menu dịch vụ. Khi khách hàng bấm chọn gói dịch vụ,
 * Frontend lấy trường 'durationMinutes' (số phút thực hiện) để cộng dồn tính tổng
 * thời gian, từ đó render chính xác các Slot giờ trống của thợ làm tóc.
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/public/services")
@RequiredArgsConstructor
public class PublicServiceController {

    private final IGetAllActiveServicesUseCase getAllActiveServicesUseCase;
    private final IGetServicesByCategoryUseCase getServicesByCategoryUseCase;
    private final IGetServiceByIdUseCase getServiceByIdUseCase;

    /**
     * 📘 FRONTEND NOTE: LẤY TOÀN BỘ MENU BẢNG GIÁ DỊCH VỤ CỦA TRUNG TÂM
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/services
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getAllServices() {
        List<ServiceResponse> data = getAllActiveServicesUseCase.execute();
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy danh mục menu bảng giá thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: LỌC DANH SÁCH DỊCH VỤ THEO MÃ DANH MỤC KHÁCH CHỌN
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/services/category/{categoryId} (Ví dụ: /api/v1/public/services/category/1)
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getServicesByCategory(@PathVariable Long categoryId) {
        List<ServiceResponse> data = getServicesByCategoryUseCase.execute(categoryId);
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy danh sách dịch vụ theo nhóm thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XEM CHI TIẾT 1 DỊCH VỤ TÓC
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/services/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(@PathVariable Long id) {
        ServiceResponse data = getServiceByIdUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy thông tin dịch vụ chi tiết thành công!"));
    }
}