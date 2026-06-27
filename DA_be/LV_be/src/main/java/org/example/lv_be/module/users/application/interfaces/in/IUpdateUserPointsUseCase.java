package org.example.lv_be.module.users.application.interfaces.in;

public interface IUpdateUserPointsUseCase {
    // Nhận vào ID của khách hàng và số điểm cần cộng thêm
    void execute(Long userId, int pointsToAdd);
}