package org.example.lv_be.module.banners.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.banners.application.dto.request.CreateBannerRequest;
import org.example.lv_be.module.banners.application.dto.request.UpdateBannerRequest;
import org.example.lv_be.module.banners.application.dto.response.BannerResponse;
import org.example.lv_be.module.banners.application.interfaces.in.IManageBannerUseCase;
import org.example.lv_be.module.banners.application.interfaces.in.IGetBannerUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {

    private final IManageBannerUseCase manageBannerUseCase;
    private final IGetBannerUseCase getBannerUseCase;

    /**
     * =========================================================================
     * HƯỚNG DẪN CHO FRONTEND (CALL API LẤY TẤT CẢ BANNER)
     * =========================================================================
     * Method: GET
     * URL: /api/v1/admin/banners
     * Headers: Authorization: Bearer <Admin_Token>
     * Content-Type: application/json
     * Response: Trả về mảng JSON chứa tất cả banner (bao gồm cả banner đang ẩn/hiện)
     * =========================================================================
     */
    @GetMapping
    public ResponseEntity<List<BannerResponse>> getAllBanners() {
        return ResponseEntity.ok(getBannerUseCase.getAllBannersForAdmin());
    }

    /**
     * =========================================================================
     * HƯỚNG DẪN CHO FRONTEND (CALL API TẠO MỚI BANNER)
     * =========================================================================
     * Method: POST
     * URL: /api/v1/admin/banners
     * Headers: Authorization: Bearer <Admin_Token>
     * Content-Type: multipart/form-data (Bắt buộc)
     * * Cấu trúc Body (FormData):
     * 1. Key: "image" -> Kiểu: File (Tấm ảnh cần upload) -> Bắt buộc.
     * 2. Key: "data"  -> Kiểu: Blob/String (Cục JSON thông tin chữ) -> Bắt buộc.
     * Định dạng cục JSON trong key "data":
     * {
     * "title": "Khuyến mãi hè rực rỡ 2026",
     * "targetUrl": "/san-pham/combo-he",
     * "position": "HOME_TOP_SLIDER",
     * "displayOrder": 1
     * }
     * * Ví dụ code Axios bên Frontend:
     * const formData = new FormData();
     * formData.append("image", fileInput.files[0]);
     * formData.append("data", new Blob([JSON.stringify(bannerData)], { type: "application/json" }));
     * axios.post('/api/v1/admin/banners', formData);
     * =========================================================================
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BannerResponse> createBanner(
            @RequestPart("data") CreateBannerRequest request,
            @RequestPart("image") MultipartFile imageFile) {
        BannerResponse response = manageBannerUseCase.createBanner(request, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * =========================================================================
     * HƯỚNG DẪN CHO FRONTEND (CALL API CẬP NHẬT BANNER)
     * =========================================================================
     * Method: PUT
     * URL: /api/v1/admin/banners/{id} (Ví dụ: /api/v1/admin/banners/5)
     * Headers: Authorization: Bearer <Admin_Token>
     * Content-Type: multipart/form-data (Bắt buộc)
     * * Quy tắc đặc biệt:
     * - Nếu thay cả ẢNH và CHỮ: Truyền đủ cả key "image" và key "data".
     * - Nếu CHỈ sửa chữ, GIỮ NGUYÊN ảnh cũ: KHÔNG TRUYỀN key "image" (hoặc truyền null),
     * chỉ truyền key "data". Hệ thống sẽ tự biết giữ lại link ảnh cũ.
     * =========================================================================
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BannerResponse> updateBanner(
            @PathVariable Long id,
            @RequestPart(value = "data", required = false) UpdateBannerRequest request,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        BannerResponse response = manageBannerUseCase.updateBanner(id, request, imageFile);
        return ResponseEntity.ok(response);
    }

    /**
     * =========================================================================
     * HƯỚNG DẪN CHO FRONTEND (CALL API BẬT/TẮT TRẠNG THÁI HIỂN THỊ)
     * =========================================================================
     * Method: PATCH
     * URL: /api/v1/admin/banners/{id}/toggle (Ví dụ: /api/v1/admin/banners/5/toggle)
     * Headers: Authorization: Bearer <Admin_Token>
     * Mô tả: Mỗi lần bấm vào nút switch trên giao diện, gửi request này lên.
     * Hệ thống tự đảo ngược true -> false hoặc false -> true.
     * =========================================================================
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleActiveStatus(@PathVariable Long id) {
        manageBannerUseCase.toggleActiveStatus(id);
        return ResponseEntity.ok().build();
    }

    /**
     * =========================================================================
     * HƯỚNG DẪN CHO FRONTEND (CALL API XÓA BANNER)
     * =========================================================================
     * Method: DELETE
     * URL: /api/v1/admin/banners/{id} (Ví dụ: /api/v1/admin/banners/5)
     * Headers: Authorization: Bearer <Admin_Token>
     * Mô tả: Xóa vĩnh viễn dữ liệu khỏi hệ thống.
     * =========================================================================
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        manageBannerUseCase.deleteBanner(id);
        return ResponseEntity.noContent().build();
    }
}