package org.example.lv_be.module.booking.presentation.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.booking.application.dto.BookingResponse;
import org.example.lv_be.module.booking.application.dto.CreateBookingRequest;
import org.example.lv_be.module.booking.application.dto.UpdateBookingStatusRequest;
import org.example.lv_be.module.booking.application.dto.modification.AddExtraServiceRequest;
import org.example.lv_be.module.booking.application.interfaces.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ==================================================================================
 * 🔐 TÀI LIỆU API DÀNH CHO PHÂN HỆ QUẢN LÝ ĐẶT LỊCH (QUẦY LỄ TÂN & THỢ STYLIST)
 * ==================================================================================
 * 🔹 BASE URL chung: http://[domain]/api/v1/admin/bookings
 * 🔹 QUYỀN TRUY CẬP: Yêu cầu Header [Authorization: Bearer token] của tài khoản ADMIN hoặc STAFF.
 * 🔹 ĐỊNH DẠNG PHẢN HỒI CHUNG (ApiResponse):
 * {
 * "success": true/false,
 * "message": "Thông báo từ hệ thống",
 * "data": { ... Dữ liệu trả về thực tế ... }
 * }
 * ==================================================================================
 */
@RestController
@RequestMapping("/api/v1/admin/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
public class AdminBookingController {

    private final IUpdateBookingStatusUseCase updateBookingStatusUseCase;
    private final IFindBookingForCheckInUseCase findBookingForCheckInUseCase;
    private final IAddExtraServiceUseCase addExtraServiceUseCase;
    private final IRemoveServiceUseCase removeServiceUseCase;
    private final ICreateWalkInBookingUseCase createWalkInBookingUseCase;

    /**
     * 📱 API 1: TÌM KIẾM LỊCH ĐẶT THEO SỐ ĐIỆN THOẠI KHÁCH HÀNG
     * ------------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL: /api/v1/admin/bookings/search
     * 🔹 PARAMS: ?phone=0912345678 (Bắt buộc, chuỗi số điện thoại khách đọc tại quầy)
     * * 💡 HƯỚNG DẪN UI/UX FRONTEND:
     * - Tại màn hình tiếp đón, Lễ tân gõ SĐT khách vào ô tìm kiếm và nhấn Enter.
     * - Kết quả trả về là một mảng [] chứa các đơn đặt lịch (sắp xếp đơn mới nhất lên đầu).
     * - Frontend hiển thị danh sách này lên Table/List để Lễ tân đối chiếu ca giờ hôm nay.
     * * 📥 RESPONSE DATA TRẢ VỀ (Mẫu trong mảng data):
     * [
     * { "id": 10, "code": "BK-A83B19", "customerId": 1, "staffId": 3, "bookingDate": "2026-06-11", "startTime": "14:30:00", "endTime": "15:30:00", "status": "CONFIRMED" }
     * ]
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> searchByPhone(@RequestParam String phone) {
        List<BookingResponse> data = findBookingForCheckInUseCase.execute(phone);
        return ResponseEntity.ok(ApiResponse.success(data, "Tìm thấy " + data.size() + " đơn đặt lịch khớp với số điện thoại."));
    }

    /**
     * 🔄 API 2: CẬP NHẬT TRẠNG THÁI LỊCH ĐẶT (ĐIỀU PHỐI VÒNG ĐỜI - STATE MACHINE)
     * ------------------------------------------------------------------------------
     * 🔹 METHOD: PUT
     * 🔹 URL: /api/v1/admin/bookings/{id}/status  (Ví dụ: /api/v1/admin/bookings/10/status)
     * 🔹 REQUEST BODY (JSON):
     * {
     * "status": "CHECKED_IN"
     * }
     * * 💡 HƯỚNG DẪN UI/UX FRONTEND (CỰC KỲ QUAN TRỌNG):
     * - Nút bấm trên giao diện sẽ gửi Enum trạng thái tương ứng ra Backend tùy theo luồng:
     * 1. Khách đến quán đọc số điện thoại -> Ấn nút [Check-in] -> Gửi "CHECKED_IN"
     * 2. Thợ bấm mời khách vào ghế cắt tóc -> Ấn nút [Bắt đầu làm] -> Gửi "IN_PROGRESS"
     * 3. Thợ làm xong, sấy tóc vuốt sáp xong -> Ấn nút [Hoàn thành ca] -> Gửi "COMPLETED" (Luồng này sẽ kích hoạt tạo hóa đơn thanh toán).
     * 4. Khách bùng lịch hoặc gọi điện hủy ca -> Ấn nút [Hủy đơn] -> Gửi "CANCELLED"
     * 5. Quá giờ hẹn 30 phút khách không đến -> Hệ thống tự động hoặc bấm -> Gửi "NO_SHOW"
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BookingResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingStatusRequest request) {
        BookingResponse data = updateBookingStatusUseCase.execute(id, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(data, "Đã cập nhật trạng thái lịch hẹn sang: " + request.getStatus()));
    }

    /**
     * 💇‍♂️ API 3: THÊM DỊCH VỤ PHÁT SINH KHI KHÁCH ĐANG LÀM TẠI QUÁN
     * ------------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL: /api/v1/admin/bookings/{id}/services/add (Ví dụ: /api/v1/admin/bookings/10/services/add)
     * 🔹 REQUEST BODY (JSON):
     * {
     * "serviceId": 5
     * }
     * * 💡 HƯỚNG DẪN UI/UX FRONTEND:
     * - Chỉ hiển thị nút [Thêm dịch vụ] khi đơn lịch đang có trạng thái là "CHECKED_IN" hoặc "IN_PROGRESS".
     * - Khi thợ tư vấn thành công gói Uốn/Nhuộm phát sinh, chọn dịch vụ từ Dropdown -> Bấm [Thêm].
     * - Backend sẽ tự động thực hiện: Cộng thêm thời gian vào ca giờ (endTime) và cộng tiền vào Bill.
     * - Sau khi API trả về success, Frontend cần gọi lại lệnh reload data để hiển thị giờ kết thúc mới.
     */
    @PostMapping("/{id}/services/add")
    public ResponseEntity<ApiResponse<Void>> addService(
            @PathVariable Long id,
            @Valid @RequestBody AddExtraServiceRequest request) {
        addExtraServiceUseCase.execute(id, request.getServiceId());
        return ResponseEntity.ok(ApiResponse.success(null, "Đã thêm dịch vụ phát sinh vào ca làm thành công!"));
    }

    /**
     * ❌ API 4: XÓA / HUỶ BỚT DỊCH VỤ ĐÃ CHỌN (KHÁCH ĐỔI Ý)
     * ------------------------------------------------------------------------------
     * 🔹 METHOD: DELETE
     * 🔹 URL: /api/v1/admin/bookings/{id}/services/remove?serviceId=5
     * 🔹 QUERY PARAMS: ?serviceId=5 (Mã ID dịch vụ cần gỡ bỏ)
     * * 💡 HƯỚNG DẪN UI/UX FRONTEND:
     * - Thiết kế nút hình [Thùng rác] hoặc dấu [X] nhỏ cạnh tên từng dịch vụ trong hóa đơn tạm tính.
     * - Chỉ cho phép bấm xóa khi đơn ở trạng thái "CHECKED_IN" hoặc "IN_PROGRESS" và tổng số lượng dịch vụ > 1.
     * - Khi click, gọi API này để hệ thống rút ngắn giờ làm của thợ và trừ tiền tương ứng.
     */
    @DeleteMapping("/{id}/services/remove")
    public ResponseEntity<ApiResponse<Void>> removeService(
            @PathVariable Long id,
            @RequestParam Long serviceId) {
        removeServiceUseCase.execute(id, serviceId);
        return ResponseEntity.ok(ApiResponse.success(null, "Đã loại bỏ dịch vụ khỏi ca làm."));
    }
    /**
     * 📘 FRONTEND NOTE: 🏪 TẠO ĐƠN PHỤC VỤ TẠI QUÁN CHO KHÁCH VÃNG LAI (WALK-IN)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/admin/bookings/walk-in
     * 🔹 REQUEST BODY (JSON):
     * {
     * "branchId": 1,
     * "customerId": null,  // Khách vãng lai không cần tài khoản thì truyền null
     * "staffId": 3,        // Chọn thợ đang rảnh tại quán
     * "bookingDate": "2026-06-11", // Ngày hôm nay
     * "startTime": "17:15:00",     // Giờ hiện tại khách bước vào tiệm
     * "serviceIds": [1, 2],        // Khách chỉ định làm dịch vụ gì luôn
     * "notes": "Khách vãng lai vào trực tiếp"
     * }
     * 🔹 UI FLOW: Lễ tân bấm nút "Khách Vãng Lai" -> Chọn thợ rảnh -> Chọn dịch vụ -> Bấm [Xác nhận]
     * -> Đơn lịch tạo ra sẽ có trạng thái CHECKED_IN ngay lập tức, thợ có thể tiến hành làm luôn.
     */
    @PostMapping("/walk-in")
    public ResponseEntity<ApiResponse<BookingResponse>> createWalkIn(@Valid @RequestBody CreateBookingRequest request) {
        BookingResponse data = createWalkInBookingUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Khởi tạo đơn phục vụ trực tiếp tại quầy thành công!"));
    }
}