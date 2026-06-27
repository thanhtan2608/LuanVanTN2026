package org.example.lv_be.module.users.presentation.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.users.application.dto.AuthRequest;
import org.example.lv_be.module.users.application.dto.AuthResponse;
import org.example.lv_be.module.users.application.dto.RegisterRequest;
import org.example.lv_be.module.users.application.interfaces.in.ILoginUseCase;
import org.example.lv_be.module.users.application.interfaces.in.IRegisterUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ILoginUseCase loginUseCase;
    private final IRegisterUseCase registerUseCase;

    /**
     * [DÀNH CHO FRONTEND] API Đăng nhập
     * - Method: POST
     * - Body: { "phone": "09...", "password": "..." }
     * - Xử lý FE: Nhận được AuthResponse, lưu 'token' vào localStorage/Cookies,
     * dùng 'role' để điều hướng (VD: ADMIN -> /admin, CUSTOMER -> /home)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = loginUseCase.execute(request);
        return ResponseEntity.ok(response); // Status 200
    }

    /**
     * [DÀNH CHO FRONTEND] API Đăng ký khách hàng mới
     * - Method: POST
     * - Body: { "phone": "...", "password": "...", "fullName": "..." }
     * - Xử lý FE: API này tự động trả về Token luôn. FE lưu token và cho login thẳng,
     * không cần bắt user nhập lại số điện thoại để login nữa.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = registerUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Status 201 (Created)
    }
}