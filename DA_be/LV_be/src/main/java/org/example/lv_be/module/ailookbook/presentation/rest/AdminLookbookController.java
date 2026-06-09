package org.example.lv_be.module.ailookbook.presentation.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.ailookbook.application.dto.CreateLookbookRequest;
import org.example.lv_be.module.ailookbook.application.dto.LookbookResponse;
import org.example.lv_be.module.ailookbook.application.dto.UpdateLookbookRequest;
import org.example.lv_be.module.ailookbook.application.interfaces.ICreateLookbookUseCase;
import org.example.lv_be.module.ailookbook.application.interfaces.IDeleteLookbookUseCase;
import org.example.lv_be.module.ailookbook.application.interfaces.IUpdateLookbookUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/lookbook")
@RequiredArgsConstructor
public class AdminLookbookController {

    private final ICreateLookbookUseCase createLookbookUseCase;
    private final IUpdateLookbookUseCase updateLookbookUseCase;
    private final IDeleteLookbookUseCase deleteLookbookUseCase;

    /**
     * 📘 FRONTEND NOTE: ĐĂNG TẢI MẪU TÓC AI MỚI LÊN BỘ SƯU TẬP
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL: /api/v1/admin/lookbook
     * 🔹 HEADERS: Content-Type = multipart/form-data, Authorization = Bearer [Token]
     * 🔹 BODY (Form-Data):
     * - "image" : [Chọn Tệp Tin Hình Ảnh Mẫu Tóc - File]
     * - "data"  : [Chuỗi JSON Cấu Hình - Text/Application-Json]
     * {
     * "title": "Tóc uốn xoăn lãng tử Hàn Quốc",
     * "description": "Phù hợp với các bạn nam mặt dài, tạo độ bồng bềnh thanh lịch.",
     * "gender": "MALE", // Định dạng chuẩn: MALE, FEMALE, UNISEX
     * "prompt": "A handsome Korean man with curly wavy layers hairstyle",
     * "hairstyleId": 12 // ID của kiểu tóc thật trong bảng hairstyles để đặt lịch
     * }
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<LookbookResponse>> createItem(
            @Valid @RequestPart("data") CreateLookbookRequest request,
            @RequestPart("image") MultipartFile imageFile) {

        LookbookResponse response = createLookbookUseCase.execute(request, imageFile);
        return ResponseEntity.ok(ApiResponse.success(response, "Thêm mẫu tóc AI mới vào bộ sưu tập thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: CẬP NHẬT THÔNG TIN MẪU TÓC AI HIỆN TẠI
     * ------------------------------------------------------------------------
     * 🔹 METHOD: PUT
     * 🔹 URL: /api/v1/admin/lookbook/{id} (Ví dụ: /api/v1/admin/lookbook/1)
     * 🔹 HEADERS: Content-Type = multipart/form-data, Authorization = Bearer [Token]
     * 🔹 BODY (Form-Data):
     * - "image" : [Chọn File Mới Nếu Muốn Đổi Ảnh - File] (Không bắt buộc truyền)
     * - "data"  : [Chuỗi JSON Cập Nhật - Text/Application-Json] (Bắt buộc)
     * {
     * "title": "Tóc uốn xoăn lãng tử Seoul V2",
     * "description": "Mô tả cập nhật mới...",
     * "gender": "MALE",
     * "prompt": "Cấu hình câu lệnh prompt mới cho AI...",
     * "hairstyleId": 12,
     * "isActive": true // Bật tắt ẩn hiện ngoài App khách hàng
     * }
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<LookbookResponse>> updateItem(
            @PathVariable Long id,
            @Valid @RequestPart("data") UpdateLookbookRequest request,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        LookbookResponse response = updateLookbookUseCase.execute(id, request, imageFile);
        return ResponseEntity.ok(ApiResponse.success(response, "Cập nhật thông tin mẫu tóc AI thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XÓA MỀM MẪU TÓC AI RA KHỎI APP KHÁCH HÀNG
     * ------------------------------------------------------------------------
     * 🔹 METHOD: DELETE
     * 🔹 URL: /api/v1/admin/lookbook/{id} (Ví dụ: /api/v1/admin/lookbook/1)
     * 🔹 HEADERS: Authorization = Bearer [Token]
     * 🔹 BODY: Trống (Empty)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        deleteLookbookUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa mềm mẫu tóc thành công (Đã ẩn khỏi giao diện khách)!"));
    }
}