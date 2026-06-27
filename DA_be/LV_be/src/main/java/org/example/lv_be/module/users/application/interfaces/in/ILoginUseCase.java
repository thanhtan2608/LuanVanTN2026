package org.example.lv_be.module.users.application.interfaces.in;

import org.example.lv_be.module.users.application.dto.AuthRequest;
import org.example.lv_be.module.users.application.dto.AuthResponse;

public interface ILoginUseCase {
    /**
     * Thực thi luồng đăng nhập chung cho toàn bộ hệ thống
     *
     * @param request Chứa thông tin số điện thoại và mật khẩu
     * @return AuthResponse chứa JWT Token và Role để phân quyền màn hình
     */
    AuthResponse execute(AuthRequest request);
}