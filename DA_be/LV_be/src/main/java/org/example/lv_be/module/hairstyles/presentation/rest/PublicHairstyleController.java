package org.example.lv_be.module.hairstyles.presentation.rest;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.FaceShape;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.hairstyles.application.dto.HairstyleDetailResponse;
import org.example.lv_be.module.hairstyles.application.dto.HairstyleResponse; // Tên interface tương ứng của bạn
import org.example.lv_be.module.hairstyles.application.interfaces.IGetAllActiveHairstylesUseCase;
import org.example.lv_be.module.hairstyles.application.interfaces.IGetHairstyleByIdUseCase;
import org.example.lv_be.module.hairstyles.application.interfaces.IGetHairstylesByFaceShapeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * =========================================================================
 * 🌍 LƯU Ý KẾT NỐI LUỒNG AI LOOKBOOK & ĐẶT LỊCH KHÁCH HÀNG (PUBLIC ENDPOINTS)
 * =========================================================================
 * 🔹 KHÔNG YÊU CẦU ĐĂNG NHẬP (Bỏ trống Header Authorization).
 * Phục vụ trực tiếp cho màn hình Catalogue, và màn hình hiển thị kết quả tư vấn
 * kiểu tóc sau khi khách hàng quét mặt qua camera điện thoại.
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/public/hairstyles")
@RequiredArgsConstructor
public class PublicHairstyleController {

    private final IGetAllActiveHairstylesUseCase getAllActiveHairstylesUseCase;
    private final IGetHairstylesByFaceShapeUseCase getHairstylesByFaceShapeUseCase;
    private final IGetHairstyleByIdUseCase getHairstyleByIdUseCase;

    /**
     * 📘 FRONTEND NOTE: LẤY TOÀN BỘ DANH SÁCH KIỂU TÓC KHÔNG GIAN MẪU CỦA SALON
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/hairstyles
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<HairstyleResponse>>> getAllActiveHairstyles() {
        List<HairstyleResponse> data = getAllActiveHairstylesUseCase.execute();
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy danh sách album kiểu tóc thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: 🧠 ENDPOINT KẾT NỐI KẾT QUẢ PHÂN TÍCH AI (GỢI Ý THEO DÁNG MẶT)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/hairstyles/face-shape/{faceShape}
     * 🔹 VÍ DỤ GỌI: /api/v1/public/hairstyles/face-shape/ROUND
     * 🔹 LOGIC FLOW: Khi module AI Lookbook quét ảnh chân dung khách hàng và trả về chuỗi kết quả
     * dáng mặt (ví dụ: ROUND, SQUARE...), Frontend bốc chuỗi đó ném vào PathVariable của API này
     * để nhận ngay danh sách các kiểu tóc che khuyết điểm tương thích tốt nhất.
     */
    @GetMapping("/face-shape/{faceShape}")
    public ResponseEntity<ApiResponse<List<HairstyleResponse>>> getHairstylesByFaceShape(@PathVariable FaceShape faceShape) {
        List<HairstyleResponse> data = getHairstylesByFaceShapeUseCase.execute(faceShape);
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy danh sách kiểu tóc gợi ý phù hợp với dáng mặt thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XEM CHI TIẾT 1 KIỂU TÓC KÈM KHUNG BẢNG GIÁ ĐỂ ĐẶT LỊCH (BOOKING WORKFLOW)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/hairstyles/{id} (Ví dụ: /api/v1/public/hairstyles/2)
     * 🔹 ĐẶC TRƯNG RESPONSE: Dữ liệu trả về (HairstyleDetailResponse) sẽ bao gồm một mảng
     * con mang tên 'actualServices'. Mảng này chứa đầy đủ thông tin Tên dịch vụ, Giá tiền và Số phút làm
     * được liên kết từ module Catalog để khách hàng tick chọn gói dịch vụ đi kèm khi nhấn nút "Đặt Lịch Ngay".
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HairstyleDetailResponse>> getHairstyleById(@PathVariable Long id) {
        HairstyleDetailResponse data = getHairstyleByIdUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy thông tin chi tiết kiểu tóc thành công!"));
    }
}