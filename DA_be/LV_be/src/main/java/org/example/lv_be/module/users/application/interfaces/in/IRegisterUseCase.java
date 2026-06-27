package org.example.lv_be.module.users.application.interfaces.in;

import org.example.lv_be.module.users.application.dto.RegisterRequest;
import org.example.lv_be.module.users.application.dto.AuthResponse;

public interface IRegisterUseCase {
    /**
     * Thực thi luồng đăng ký tài khoản dành riêng cho Khách Hàng (Customer)
     *
     * @param request Chứa thông tin đăng ký (SĐT, Mật khẩu, Họ tên)
     * @return AuthResponse chứa Token và thông tin cơ bản để app tự động login
     */
    AuthResponse execute(RegisterRequest request);
}