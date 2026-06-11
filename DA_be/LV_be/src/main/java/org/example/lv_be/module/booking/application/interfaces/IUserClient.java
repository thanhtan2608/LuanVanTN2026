package org.example.lv_be.module.booking.application.interfaces;

import java.util.Optional;

public interface IUserClient {
    // Nhận vào số điện thoại từ Booking và tìm ra ID của User (Khách hàng) bên module Users
    Optional<Long> findUserIdByPhone(String phone);
}