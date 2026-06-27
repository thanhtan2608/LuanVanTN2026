package org.example.lv_be.module.users.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.users.application.dto.UserProfileResponse;
import org.example.lv_be.module.users.application.interfaces.in.IGetUserProfileUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final IGetUserProfileUseCase getUserProfileUseCase;

    /**
     * [DÀNH CHO FRONTEND] API Lấy thông tin cá nhân của phiên đăng nhập hiện tại
     * - Method: GET
     * - Header: Authorization: Bearer <Token>
     * - Xử lý FE: Gọi API này ngay khi App load hoặc F5 trang (với token còn hạn).
     * Nếu trả về 401/403 -> Xóa local storage và đá về trang /login.
     * Nếu trả về 200 -> Lấy 'points' hoặc 'baseSalary' để hiển thị UI tùy Role.
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(Principal principal) {
        // Lấy định danh (thường là SĐT) từ Principal do Spring Security trích xuất từ JWT Token
        String phone = principal.getName();

        // Gọi Use Case xử lý logic lấy thông tin từ DB (tự động phân loại Customer/Employee)
        UserProfileResponse response = getUserProfileUseCase.execute(phone);

        // Trả về data kèm HTTP Status 200 (OK)
        return ResponseEntity.ok(response);
    }
}