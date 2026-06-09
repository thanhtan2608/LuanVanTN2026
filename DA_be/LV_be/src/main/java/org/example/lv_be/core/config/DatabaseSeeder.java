package org.example.lv_be.core.config;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.Role;
import org.example.lv_be.module.users.application.interfaces.IPasswordHasher;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    // Vẫn tuân thủ Clean Architecture: Chỉ gọi qua Interface
    private final IUserRepository userRepository;
    private final IPasswordHasher passwordHasher;

    @Override
    public void run(String... args) {
        String adminPhone = "0999999999"; // SĐT mặc định cho Admin

        // 1. Kiểm tra xem Admin mặc định đã tồn tại chưa
        if (!userRepository.existsByPhone(adminPhone)) {

            // 2. Nếu chưa có, tiến hành tạo mới
            User admin = new User();
            admin.setPhone(adminPhone);
            admin.setFullName("Super Admin");
            admin.setPassword(passwordHasher.encode("123456")); // Mật khẩu mặc định
            admin.setRole(Role.ADMIN);

            // 3. Lưu xuống Database
            userRepository.save(admin);

            System.out.println("==================================================");
            System.out.println("✅ Đã khởi tạo tài khoản ADMIN hệ thống!");
            System.out.println("📱 SĐT: " + adminPhone);
            System.out.println("🔑 Mật khẩu: 123456");
            System.out.println("==================================================");
        }
    }
}