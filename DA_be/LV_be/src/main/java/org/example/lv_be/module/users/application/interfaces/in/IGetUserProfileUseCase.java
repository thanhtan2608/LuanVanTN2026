package org.example.lv_be.module.users.application.interfaces.in;

import org.example.lv_be.module.users.application.dto.UserProfileResponse;

public interface IGetUserProfileUseCase {
    /**
     * Lấy thông tin chi tiết của người dùng dựa trên số điện thoại (từ Token)
     * * @param phone Số điện thoại của người dùng đang đăng nhập
     * @return UserProfileResponse chứa thông tin cá nhân (và điểm/lương tùy role)
     */
    UserProfileResponse execute(String phone);
}