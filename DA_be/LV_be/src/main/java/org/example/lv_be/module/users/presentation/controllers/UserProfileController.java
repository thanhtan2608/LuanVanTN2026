package org.example.lv_be.module.users.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.users.application.dto.UserProfileResponse;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final IUserRepository userRepository;

    /**
     * 📘 FRONTEND NOTE: LẤY THÔNG TIN HỒ SƠ, ĐIỂM SỐ VÀ HẠNG THÀNH VIÊN
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL: /api/v1/users/profile/me
     * 🔹 HEADER: Authorization: Bearer {token_của_khách_hàng}
     * 🔹 LOGIC: Backend tự động bóc tách Token để biết user nào đang đăng nhập,
     * từ đó trả về đúng điểm và hạng của user đó.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile() {

        // 1. Lấy Username/Phone từ JWT Token hiện tại
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Query DB lấy dữ liệu (Nên dùng UseCase, ở đây mình viết tắt qua Repo để bạn dễ hình dung)
        User user = userRepository.findByPhone(currentUsername)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin tài khoản"));

        // 3. Map sang DTO trả về cho Frontend
        UserProfileResponse response = UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .points(user.getPoints())
                .memberTier(user.getMemberTier())
                .build();

        return ResponseEntity.ok(ApiResponse.success(response, "Lấy hồ sơ và điểm thành công!"));
    }
}