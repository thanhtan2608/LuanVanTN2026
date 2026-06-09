package org.example.lv_be.module.ailookbook.presentation.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.ailookbook.application.dto.AiStyleResponse;
import org.example.lv_be.module.ailookbook.application.dto.AiTryOnRequest;
import org.example.lv_be.module.ailookbook.application.dto.LookbookDetailResponse;
import org.example.lv_be.module.ailookbook.application.dto.LookbookResponse;
import org.example.lv_be.module.ailookbook.application.interfaces.IGetActiveLookbooksUseCase;
import org.example.lv_be.module.ailookbook.application.interfaces.IGetLookbookDetailUseCase;
import org.example.lv_be.module.ailookbook.application.interfaces.IGetUserAiHistoryUseCase;
import org.example.lv_be.module.ailookbook.application.interfaces.IRequestAiTryOnUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lookbook")
@RequiredArgsConstructor
public class CustomerLookbookController {

    private final IGetActiveLookbooksUseCase getActiveLookbooksUseCase;
    private final IGetLookbookDetailUseCase getLookbookDetailUseCase;
    private final IRequestAiTryOnUseCase requestAiTryOnUseCase;
    private final IGetUserAiHistoryUseCase getUserAiHistoryUseCase;

    /**
     * 📘 FRONTEND NOTE: LẤY TOÀN BỘ KHO ẢNH MẪU TÓC AI HIỂN THỊ LÊN GALLERY
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL: /api/v1/lookbook
     * 🔹 PARAMETERS (Optional Filter):
     * - ?gender=MALE   (Chỉ lọc lấy mẫu tóc Nam)
     * - ?gender=FEMALE (Chỉ lọc lấy mẫu tóc Nữ)
     * - Để trống       (Lấy toàn bộ không lọc)
     * 🔹 RESPONSE DATA EX: List danh sách gồm id, title, description, imageUrl...
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<LookbookResponse>>> getAllActive(
            @RequestParam(value = "gender", required = false) String gender) {
        List<LookbookResponse> list = getActiveLookbooksUseCase.execute(gender);
        return ResponseEntity.ok(ApiResponse.success(list, "Lấy danh sách bộ sưu tập tóc mẫu thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XEM CHI TIẾT 1 MẪU TÓC VÀ LẤY BẢNG GIÁ ĐỜI THẬT ĐỂ ĐẶT LỊCH
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL: /api/v1/lookbook/{id} (Ví dụ: /api/v1/lookbook/5)
     * 🔹 RESPONSE DATA STRUCTURE: Trả ra thông tin ảnh mẫu và danh sách mảng combo dịch vụ đi kèm
     * {
     * "id": 5, "title": "Tóc uốn Layer", "imageUrl": "https://...",
     * "actualServices": [
     * { "id": 101, "serviceName": "Combo Cắt tạo kiểu Layer", "price": 150000.0, "durationMinutes": 30 },
     * { "id": 102, "serviceName": "Uốn tóc Layer Hàn Quốc", "price": 350000.0, "durationMinutes": 60 }
     * ]
     * }
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LookbookDetailResponse>> getDetail(@PathVariable Long id) {
        LookbookDetailResponse detail = getLookbookDetailUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(detail, "Lấy chi tiết mẫu tóc và bảng giá dịch vụ đi kèm thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: KÍCH HOẠT TIẾN TRÌNH AI THỬ/THIẾT KẾ KIỂU TÓC MỚI (🚨 TRỌNG TÂM)
     * ------------------------------------------------------------------------
     * 📌 CHÚ Ý CỰC LỚN CHO FRONTEND: Vì API dùng @ModelAttribute bọc đối tượng chứa file,
     * Frontend KHÔNG ĐƯỢC truyền chuỗi JSON, mà phải truyền các trường PHẲNG (Flat Fields)
     * nằm chung trong một Body Key-Value Form-Data duy nhất như sau:
     * * 🔹 METHOD: POST
     * 🔹 URL: /api/v1/lookbook/try-on
     * 🔹 HEADERS: Content-Type = multipart/form-data, Authorization = Bearer [Token]
     * 🔹 BODY (Form-Data phẳng):
     * - Key 1: lookbookItemId (Value là số, ví dụ: 5)
     * - Key 2: customerImage  (Value là Tệp Tin ảnh chụp mặt thật của khách từ Camera/Library)
     * * 🔹 CƠ CHẾ RESPONSE: API trả về ngay lập tức trạng thái "status": "PENDING" kèm ID tác vụ.
     * Frontend nhận được phản hồi này lập tức bật màn hình xoay Loading: "Hệ thống đang vẽ..."
     * Sau đó 5-10 giây, Frontend chủ động gọi API /history (hoặc dùng Polling/SST) để lấy ảnh kết quả mới về.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AiStyleResponse>> requestAiTryOn(
            @Valid @ModelAttribute AiTryOnRequest request) {

        // GIẢ ĐỊNH THỰC TẾ: Bóc tách mã định danh từ Token người dùng đang đăng nhập bảo mật
        // Ở đây gán cứng userId = 1L để phục vụ test thông luồng nhanh chóng.
        // Khi tích hợp Security, thay bằng: SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long authenticatedUserId = 1L;

        AiStyleResponse response = requestAiTryOnUseCase.execute(authenticatedUserId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Hệ thống đã tiếp nhận ảnh của bạn, tiến trình AI đang vẽ ngầm..."));
    }

    /**
     * 📘 FRONTEND NOTE: LẤY LỊCH SỬ THỬ TÓC VÀ NHẬN ẢNH THÀNH PHẨM TỪ AI THẦT
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL: /api/v1/lookbook/history
     * 🔹 HEADERS: Authorization = Bearer [Token]
     * 🔹 RESPONSE DATA STRUCTURE: Trả về danh sách nhật ký đổi tóc của khách hàng (Mới nhất xếp đầu)
     * [
     * {
     * "id": 89,
     * "sourceImageUrl": "https://ik.imagekit.io/.../anh_mat_that_cua_khach.jpg",
     * "resultImageUrl": "data:image/jpeg;base64,/9j/4AAQSkZJRg...", // Chuỗi Base64 ảnh thật từ Google
     * "status": "SUCCESS", // Các trạng thái: PENDING (Chờ), PROCESSING (Đang vẽ), SUCCESS (Xong), FAILED (Lỗi)
     * "errorMessage": null
     * }
     * ]
     * 📌 CHÚ Ý HIỂN THỊ: Trường 'resultImageUrl' trả về chuỗi Data URL Base64.
     * Frontend chỉ cần ném thẳng chuỗi này vào thuộc tính src của thẻ ảnh <img src="{resultImageUrl}">
     * là giao diện tự động hiển thị ra bức ảnh lung linh, không cần tốn công xử lý gì thêm!
     */
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<AiStyleResponse>>> getMyHistory() {
        Long authenticatedUserId = 1L; // Thay bằng bóc tách token thực tế giống như hàm trên
        List<AiStyleResponse> history = getUserAiHistoryUseCase.execute(authenticatedUserId);
        return ResponseEntity.ok(ApiResponse.success(history, "Lấy danh sách lịch sử thiết kế tóc AI của bạn thành công!"));
    }
}