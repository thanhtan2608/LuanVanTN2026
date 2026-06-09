package org.example.lv_be.module.users.presentation.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.users.application.dto.*;
import org.example.lv_be.module.users.application.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * =========================================================================
 * 🔐 LƯU Ý CHUNG CHO FRONTEND VỀ KHÔNG GIAN XÁC THỰC (AUTH SECURITY NOTE)
 * =========================================================================
 * 1. Các API trong Controller này KHÔNG YÊU CẦU TOKEN khi gọi (ngoại trừ /logout).
 * 2. Khi Đăng ký hoặc Đăng nhập thành công, Server trả về cục Data chứa 'accessToken'.
 * Frontend phải lưu Token này vào LocalStorage / AsyncStorage / Cookies.
 * 3. Kể từ các lượt gọi API sau (Xem lịch sử, Đổi tóc AI, Đặt lịch...), Frontend bắt buộc
 * phải đính kèm Token vào Header theo chuẩn: Authorization = Bearer [Token]
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 📘 FRONTEND NOTE: ĐĂNG KÝ TÀI KHOẢN KHÁCH HÀNG MỚI
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/auth/register
     * 🔹 HEADERS: Content-Type = application/json
     * 🔹 REQUEST BODY (JSON):
     * {
     * "fullName": "Nguyễn Khách Hàng",
     * "phone": "0912345678",
     * "password": "MậtKhẩuCủaKhách123@",
     * "email": "khachhang@example.com" // Có thể để trống tùy thuộc vào Validation DTO
     * }
     * 🔹 RESPONSE EX (success): Trả về luôn Token để tự động đăng nhập sau khi đăng ký
     * {
     * "success": true,
     * "code": 200,
     * "message": "Đăng ký tài khoản thành công!",
     * "data": {
     * "accessToken": "eyJhbGciOiJIUzI1NiIsIn...", // Token để găm vào các API sau
     * "tokenType": "Bearer",
     * "role": "CUSTOMER"
     * }
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.registerCustomer(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Đăng ký tài khoản thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: ĐĂNG NHẬP HỆ THỐNG (DÙNG CHUNG CHO TẤT CẢ CÁC ROLE)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/auth/login
     * 🔹 HEADERS: Content-Type = application/json
     * 🔹 REQUEST BODY (JSON):
     * {
     * "username": "0912345678", // Thường là Số điện thoại hoặc Email đăng nhập
     * "password": "MậtKhẩu123@"
     * }
     * 🔹 RESPONSE EX (success): Trả về Token và phân quyền (Role) để Frontend điều hướng trang UI
     * {
     * "success": true,
     * "code": 200,
     * "message": "Đăng nhập thành công!",
     * "data": {
     * "accessToken": "eyJhbGciOiJIUzI1NiIsIn...",
     * "tokenType": "Bearer",
     * "role": "CUSTOMER" // Hoặc "STAFF", "ADMIN" tùy tài khoản để điều hướng Giao diện phù hợp
     * }
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Đăng nhập thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: YÊU CẦU QUÊN MẬT KHẨU (GỬI MÃ OTP)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/auth/forgot-password
     * 🔹 HEADERS: Content-Type = application/json
     * 🔹 REQUEST BODY (JSON):
     * {
     * "phone": "0912345678" // Số điện thoại cần khôi phục mật khẩu
     * }
     * 🔹 RESPONSE EX (success): Nhận phản hồi này xong, Frontend chuyển sang màn hình Nhập OTP + Mật khẩu mới
     * {
     * "success": true,
     * "code": 200,
     * "message": "Mã OTP đã được gửi đến số điện thoại của bạn!",
     * "data": null
     * }
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> requestForgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.requestForgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Mã OTP đã được gửi đến số điện thoại của bạn!"));
    }

    /**
     * 📘 FRONTEND NOTE: ĐẶT LẠI MẬT KHẨU MỚI (XÁC THỰC BẰNG OTP)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/auth/reset-password
     * 🔹 HEADERS: Content-Type = application/json
     * 🔹 REQUEST BODY (JSON):
     * {
     * "phone": "0912345678",
     * "otpCode": "123456", // Mã số OTP khách nhận được qua SMS/Hệ thống
     * "newPassword": "MậtKhẩuMới123@"
     * }
     * 🔹 RESPONSE EX (success): Thành công, Frontend điều hướng khách quay lại trang Đăng nhập (/login)
     * {
     * "success": true,
     * "code": 200,
     * "message": "Đặt lại mật khẩu thành công! Bạn có thể đăng nhập.",
     * "data": null
     * }
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Đặt lại mật khẩu thành công! Bạn có thể đăng nhập."));
    }

    /**
     * 📘 FRONTEND NOTE: ĐĂNG XUẤT KHỎI HỆ THỐNG (HỦY PHIÊN TOKEN)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/auth/logout
     * 🔹 HEADERS: Authorization = Bearer [Token_Hiện_Tại_Của_User] (Bắt buộc để Server hủy session ngầm)
     * 🔹 BODY: Trống (Empty)
     * 🔹 RESPONSE EX (success): Nhận xong, Frontend xóa sạch dữ liệu Token ở LocalStorage và đẩy về trang Welcome
     * {
     * "success": true,
     * "code": 200,
     * "message": "Đăng xuất thành công!",
     * "data": null
     * }
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
        return ResponseEntity.ok(ApiResponse.success(null, "Đăng xuất thành công!"));
    }
}