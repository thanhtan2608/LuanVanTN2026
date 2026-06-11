package org.example.lv_be.module.booking.presentation.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.booking.application.dto.AvailableSlotsResponse;
import org.example.lv_be.module.booking.application.dto.BookingDetailResponse;
import org.example.lv_be.module.booking.application.dto.BookingResponse;
import org.example.lv_be.module.booking.application.dto.CreateBookingRequest;
import org.example.lv_be.module.booking.application.interfaces.ICreateBookingUseCase;
import org.example.lv_be.module.booking.application.interfaces.IGetAvailableSlotsUseCase;
import org.example.lv_be.module.booking.application.interfaces.IGetBookingByIdUseCase;
import org.example.lv_be.module.booking.application.interfaces.IGetCustomerBookingHistoryUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * =========================================================================
 * 🌍 LƯU Ý CHO FRONTEND TRÊN APP APP MOBILE (CUSTOMER BOOKING FLOW)
 * =========================================================================
 * Phân hệ điều phối luồng tự đặt chỗ của khách hàng từ xa.
 * 🔹 Luồng xem ô giờ trống (Slots) KHÔNG cần token đăng nhập.
 * 🔹 Luồng Đặt lịch và Xem lịch sử yêu cầu Token Khách hàng (Bearer [Token]).
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/public/bookings")
@RequiredArgsConstructor
public class PublicBookingController {

    private final ICreateBookingUseCase createBookingUseCase;
    private final IGetAvailableSlotsUseCase getAvailableSlotsUseCase;
    private final IGetCustomerBookingHistoryUseCase getCustomerBookingHistoryUseCase;
    private final IGetBookingByIdUseCase getBookingByIdUseCase;

    /**
     * 📘 FRONTEND NOTE: KHÁCH HÀNG TIẾN HÀNH BẤM ĐẶT LỊCH HẸN LÀM TÓC
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/public/bookings
     * 🔹 REQUEST BODY (JSON):
     * {
     * "branchId": 1,
     * "customerId": 12, // Để trống/null nếu đặt ẩn danh không cần tài khoản
     * "staffId": 3,     // ID thợ Stylist được chọn
     * "hairstyleId": 2, // ID kiểu tóc găm từ module AI Lookbook sang (Có thể null)
     * "bookingDate": "2026-06-15",
     * "startTime": "14:30:00",
     * "serviceIds": [1, 4], // List các ID dịch vụ (Ví dụ: 1 là Cắt, 4 là Nhuộm)
     * "notes": "Cắt kỹ hai bên side giúp mình"
     * }
     * 🔹 CƠ CHẾ NGẦM: Hệ thống tự gọi Catalog tính tổng số phút, tự sinh ra endTime
     * và chặn đứng nếu khung giờ đó thợ 3 đã bị người khác giật chỗ mất (Optimistic Locking).
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> customerCreateBooking(@Valid @RequestBody CreateBookingRequest request) {
        BookingResponse data = createBookingUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Bạn đã đặt lịch hẹn làm tóc thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: 🧠 QUÉT TÌM CÁC Ô GIỜ CÒN TRỐNG CỦA THỢ (AVAILABLE SLOTS)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/bookings/slots?staffId=3&date=2026-06-15
     * 🔹 PARAMETERS: staffId (Mã thợ), date (Ngày muốn đến YYYY-MM-DD)
     * 🔹 RESPONSE EX: { "staffId": 3, "availableSlots": ["08:00", "08:30", "10:00", "14:30",...] }
     * 🔹 UI DEPLOY: Khi khách chọn ngày và chọn thợ, lập tức call API này để lấy mảng chuỗi giờ,
     * các ô giờ không nằm trong danh sách trả về này sẽ bị Disabled (bị xám màu không cho click).
     */
    @GetMapping("/slots")
    public ResponseEntity<ApiResponse<AvailableSlotsResponse>> getAvailableSlots(
            @RequestParam Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        AvailableSlotsResponse data = getAvailableSlotsUseCase.execute(staffId, date);
        return ResponseEntity.ok(ApiResponse.success(data, "Tải danh sách khung giờ trống của thợ thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XEM LỊCH SỬ/DANH SÁCH LỊCH HẸN ĐÃ ĐẶT CỦA CÁ NHÂN KHÁCH
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/bookings/history/{customerId}
     */
    @GetMapping("/history/{customerId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getCustomerHistory(@PathVariable Long customerId) {
        List<BookingResponse> data = getCustomerBookingHistoryUseCase.execute(customerId);
        return ResponseEntity.ok(ApiResponse.success(data, "Tải lịch sử đặt chỗ thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: KHÁCH XEM CHI TIẾT VÉ ĐẶT LỊCH HẸN CỦA MÌNH
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/bookings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingDetailResponse>> getBookingDetailForCustomer(@PathVariable Long id) {
        BookingDetailResponse data = getBookingByIdUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy thông tin vé đặt lịch chi tiết thành công!"));
    }
}