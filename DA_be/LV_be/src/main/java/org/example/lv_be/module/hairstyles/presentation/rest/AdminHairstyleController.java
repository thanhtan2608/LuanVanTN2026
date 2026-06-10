package org.example.lv_be.module.hairstyles.presentation.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.hairstyles.application.dto.CreateHairstyleRequest;
import org.example.lv_be.module.hairstyles.application.dto.HairstyleResponse;
import org.example.lv_be.module.hairstyles.application.dto.SyncHairstyleServicesRequest;
import org.example.lv_be.module.hairstyles.application.dto.UpdateHairstyleRequest;
import org.example.lv_be.module.hairstyles.application.interfaces.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * =========================================================================
 * 🔐 LƯU Ý PHÂN QUYỀN HỆ THỐNG CHO FRONTEND (ADMIN HAIRSTYLES SECURITY)
 * =========================================================================
 * Tất cả các cổng chức năng tại file này yêu cầu quyền quản trị viên cao cấp.
 * Frontend bắt buộc đính kèm Header: Authorization = Bearer [Admin_Token]
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/admin/hairstyles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminHairstyleController {

    private final ICreateHairstyleUseCase createHairstyleUseCase;
    private final IUpdateHairstyleUseCase updateHairstyleUseCase;
    private final IDeleteHairstyleUseCase deleteHairstyleUseCase;
    private final ISyncHairstyleServicesUseCase syncHairstyleServicesUseCase;

    /**
     * 📘 FRONTEND NOTE: THÊM MỚI KIỂU TÓC CATALOGUE KÈM ẢNH MẪU
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/admin/hairstyles
     * 🔹 HEADERS: Content-Type = multipart/form-data
     * 🔹 BODY PAYLOAD (Form-data):
     * - request (application/json): { "name": "Mulllet Layer Cá Tính", "faceShape": "OVAL", "description": "Hợp với khuôn mặt trái xoan thanh tú" }
     * - file (binary): [Tệp tin ảnh kiểu tóc lấy từ thư viện hoặc camera]
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<HairstyleResponse>> createHairstyle(
            @Valid @RequestPart("request") CreateHairstyleRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        HairstyleResponse data = createHairstyleUseCase.execute(request, file);
        return ResponseEntity.ok(ApiResponse.success(data, "Đăng ký kiểu tóc mới lên hệ thống thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: CHỈNH SỬA THÔNG TIN KIỂU TÓC VÀ CẬP NHẬT LẠI ẢNH MẪU
     * ------------------------------------------------------------------------
     * 🔹 METHOD: PUT
     * 🔹 URL CHUẨN: /api/v1/admin/hairstyles/{id} (Ví dụ: /api/v1/admin/hairstyles/1)
     * 🔹 HEADERS: Content-Type = multipart/form-data
     * 🔹 BODY PAYLOAD (Form-data):
     * - request (application/json): { "name": "Mullet Layer 2026", "faceShape": "ROUND", "description": "Mẫu mới cập nhật che khuyết điểm mặt tròn", "active": true }
     * - file (binary): [Để trống trường này nếu không muốn thay đổi ảnh cũ]
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<HairstyleResponse>> updateHairstyle(
            @PathVariable Long id,
            @Valid @RequestPart("request") UpdateHairstyleRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        HairstyleResponse data = updateHairstyleUseCase.execute(id, request, file);
        return ResponseEntity.ok(ApiResponse.success(data, "Cập nhật dữ liệu kiểu tóc thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: ĐỒNG BỘ / GẮN DANH SÁCH DỊCH VỤ ÁP DỤNG CHO KIỂU TÓC (MANY-TO-MANY)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/admin/hairstyles/{id}/services (Ví dụ: /api/v1/admin/hairstyles/1/services)
     * 🔹 HEADERS: Content-Type = application/json
     * 🔹 REQUEST BODY (JSON):
     * {
     * "serviceIds": [1, 3, 5] // Mảng các ID gói dịch vụ (Cắt, Uốn, Nhuộm) lấy từ Catalog
     * }
     * 🔹 CƠ CHẾ: Hệ thống tự động dọn sạch các liên kết cũ trong bảng trung gian và ghi đè loạt liên kết mới.
     */
    @PostMapping("/{id}/services")
    public ResponseEntity<ApiResponse<Void>> syncAssociatedServices(
            @PathVariable Long id,
            @Valid @RequestBody SyncHairstyleServicesRequest request) {
        syncHairstyleServicesUseCase.execute(id, request.getServiceIds());
        return ResponseEntity.ok(ApiResponse.success(null, "Đồng bộ liên kết dịch vụ kỹ thuật cho kiểu tóc thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XÓA MỀM KIỂU TÓC KHỎI HỆ THỐNG
     * ------------------------------------------------------------------------
     * 🔹 METHOD: DELETE
     * 🔹 URL CHUẨN: /api/v1/admin/hairstyles/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHairstyle(@PathVariable Long id) {
        deleteHairstyleUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa kiểu tóc khỏi danh mục thành công!"));
    }
}