package org.example.lv_be.module.users.presentation.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.users.application.dto.ChangePasswordRequest;
import org.example.lv_be.module.users.application.dto.UserProfileResponse;
import org.example.lv_be.module.users.application.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * =========================================================================
 * 👤 LƯU Ý QUAN TRỌNG CHO FRONTEND (USER PROFILE & SECURITY CONTEXT)
 * =========================================================================
 * 1. Ngoại trừ endpoint test dashboard, toàn bộ API trong này dùng để xử lý
 * cho CHÍNH USER ĐANG ĐĂNG NHẬP.
 * 2. Frontend KHÔNG CẦN truyền 'userId' hay 'phone' lên Body/Param. Hệ thống
 * Backend sẽ tự động bóc tách Token từ Header để nhận diện xem ai đang gọi.
 * 3. Bắt buộc đính kèm Header: Authorization = Bearer [Token]
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    /**
     * 📘 FRONTEND NOTE: LẤY THÔNG TIN HỒ SƠ (PROFILE) CỦA BẢN THÂN
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/users/me
     * 🔹 HEADERS: Authorization = Bearer [User_Token]
     * 🔹 BODY: Trống (Empty)
     * 🔹 RESPONSE EX (success): Trả về chi tiết trang cá nhân của tài khoản đang đăng nhập
     * {
     * "success": true,
     * "code": 200,
     * "message": "Lấy thông tin hồ sơ thành công!",
     * "data": {
     * "id": 15,
     * "fullName": "Nguyễn Đức Anh",
     * "phone": "0912345678",
     * "email": "ducanh@gmail.com",
     * "role": "CUSTOMER", // Hoặc STAFF, ADMIN
     * "avatarUrl": "https://ik.imagekit.io/..."
     * }
     * }
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {

        String currentPhone = userDetails.getUsername();
        UserProfileResponse profile = authService.getProfile(currentPhone);

        return ResponseEntity.ok(ApiResponse.success(profile, "Lấy thông tin hồ sơ thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: ENDPOINT TEST PHÂN QUYỀN (CHỈ ADMIN ĐƯỢC CHẠM)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/users/admin-dashboard
     * 🔹 HEADERS: Authorization = Bearer [Phải là Token của ACCOUNT ADMIN]
     * 🔹 RESPONSE EX (success): 200 OK kèm chuỗi String chào mừng
     * 🔹 RESPONSE EX (fail do sai quyền): 403 Forbidden nếu Khách/Thợ cố tình gọi vào
     */
    @GetMapping("/admin-dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> testAdminRoute() {
        return ResponseEntity.ok(ApiResponse.success("Chào sếp! Đây là dữ liệu mật chỉ Admin được xem.", "Thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: THAY ĐỔI MẬT KHẨU KHI ĐANG ĐĂNG NHẬP
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/users/change-password
     * 🔹 HEADERS: Content-Type = application/json, Authorization = Bearer [User_Token]
     * 🔹 REQUEST BODY (JSON):
     * {
     * "oldPassword": "MatKhauCu123@",
     * "newPassword": "MatKhauMoi456@"
     * }
     * 🔹 RESPONSE EX (success): Thành công, Frontend hiển thị Toast thông báo và có thể bắt khách login lại nếu muốn
     * {
     * "success": true,
     * "code": 200,
     * "message": "Đổi mật khẩu thành công!",
     * "data": null
     * }
     * 🔹 RESPONSE EX (fail - nhập sai mật khẩu cũ): 400 Bad Request
     * {
     * "success": false,
     * "code": 400,
     * "message": "Mật khẩu cũ không chính xác!",
     * "data": null
     * }
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {

        String phone = userDetails.getUsername();
        authService.changePassword(phone, request);
        return ResponseEntity.ok(ApiResponse.success(null, "Đổi mật khẩu thành công!"));
    }
}