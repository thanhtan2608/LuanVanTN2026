package org.example.lv_be.module.users.infrastructure.external;

import org.example.lv_be.module.users.application.interfaces.out.ISmsService;
import org.springframework.stereotype.Service;

@Service
public class MockSmsServiceImpl implements ISmsService {

    @Override
    public void sendOtpSms(String phoneNumber, String otpCode) {
        // Thay vì gọi API thật, in ra console để xem mã OTP
        System.out.println("======================================");
        System.out.println("MOCK SMS: Đang gửi OTP [" + otpCode + "] tới SĐT: " + phoneNumber);
        System.out.println("======================================");
    }

    @Override
    public void sendWelcomeSms(String phoneNumber, String fullName) {
        System.out.println("MOCK SMS: Chào mừng " + fullName + " đã đến với Tiệm Cắt Tóc Tấn & Thủ !");
    }
}