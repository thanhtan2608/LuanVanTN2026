package org.example.lv_be.module.users.application.interfaces;

public interface ISmsService {
    /**
     * Gửi tin nhắn chứa mã OTP đến số điện thoại của người dùng
     * @param phoneNumber Số điện thoại người nhận
     * @param otpCode Mã OTP 6 số
     */
    void sendOtpSms(String phoneNumber, String otpCode);

    /**
     * Gửi tin nhắn chào mừng khi đăng ký thành công
     */
    void sendWelcomeSms(String phoneNumber, String fullName);
}