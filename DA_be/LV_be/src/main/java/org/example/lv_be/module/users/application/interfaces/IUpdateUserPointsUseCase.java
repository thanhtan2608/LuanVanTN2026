package org.example.lv_be.module.users.application.interfaces;

public interface IUpdateUserPointsUseCase {
    // Nhận vào ID của khách hàng và số điểm cần cộng thêm
    void execute(Long userId, int pointsToAdd);
}