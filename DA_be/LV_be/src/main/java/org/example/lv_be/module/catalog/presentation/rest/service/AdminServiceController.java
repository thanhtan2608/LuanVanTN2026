package org.example.lv_be.module.catalog.presentation.rest.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.catalog.application.dto.service.CreateServiceRequest;
import org.example.lv_be.module.catalog.application.dto.service.ServiceResponse;
import org.example.lv_be.module.catalog.application.dto.service.UpdateServiceRequest;
import org.example.lv_be.module.catalog.application.interfaces.service.ICreateServiceUseCase;
import org.example.lv_be.module.catalog.application.interfaces.service.IDeleteServiceUseCase;
import org.example.lv_be.module.catalog.application.interfaces.service.IUpdateServiceUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * =========================================================================
 * 🔐 LƯU Ý KỸ THUẬT QUAN TRỌNG CHO FRONTEND (MULTIPART FORM DATA)
 * =========================================================================
 * Vì API thêm/sửa dịch vụ có đính kèm file ảnh minh họa lên mây ImageKit:
 * 1. Bắt buộc đặt Content-Type của Request là: multipart/form-data
 * 2. Cấu trúc đẩy lên gồm 2 phần dữ liệu (Parts):
 * - Part 1 tên là "request": Chứa chuỗi JSON thông tin text dịch vụ (Cần chỉ định Content-Type là application/json)
 * - Part 2 tên là "file": Chứa file ảnh nhị phân chọn từ máy điện thoại/PC.
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/admin/services")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceController {

    private final ICreateServiceUseCase createServiceUseCase;
    private final IUpdateServiceUseCase updateServiceUseCase;
    private final IDeleteServiceUseCase deleteServiceUseCase;

    /**
     * 📘 FRONTEND NOTE: THÊM DỊCH VỤ MỚI KÈM ẢNH MINH HỌA
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/admin/services
     * 🔹 BODY PAYLOAD (Form-data):
     * - request (application/json): { "categoryId": 1, "name": "Cắt tóc Layer Nam", "price": 100000, "durationMinutes": 30 }
     * - file (binary): [Ảnh đại diện gói cắt tóc]
     * 🔹 RESPONSE EX: Trả về Object có thêm link ảnh 'imageUrl' từ ImageKit
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ServiceResponse>> createService(
            @Valid @RequestPart("request") CreateServiceRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        ServiceResponse data = createServiceUseCase.execute(request, file);
        return ResponseEntity.ok(ApiResponse.success(data, "Thêm gói dịch vụ mới thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: CẬP NHẬT GÓI DỊCH VỤ (CÓ THỂ THAY ẢNH HOẶC KHÔNG)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: PUT
     * 🔹 URL CHUẨN: /api/v1/admin/services/{id}
     * 🔹 BODY PAYLOAD (Form-data):
     * - request (application/json): { "categoryId": 1, "name": "Cắt Tóc VIP Layer", "price": 120000, "durationMinutes": 35, "active": true }
     * - file (binary): [Nếu không muốn đổi ảnh, để trống trống trường file này]
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(
            @PathVariable Long id,
            @Valid @RequestPart("request") UpdateServiceRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        ServiceResponse data = updateServiceUseCase.execute(id, request, file);
        return ResponseEntity.ok(ApiResponse.success(data, "Cập nhật thông tin dịch vụ thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XÓA MỀM GÓI DỊCH VỤ KHỎI MENU HỆ THỐNG
     * ------------------------------------------------------------------------
     * 🔹 METHOD: DELETE
     * 🔹 URL CHUẨN: /api/v1/admin/services/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        deleteServiceUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa gói dịch vụ thành công!"));
    }
}