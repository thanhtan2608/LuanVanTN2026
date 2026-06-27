package org.example.lv_be.module.users.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.users.application.dto.AuthRequest;
import org.example.lv_be.module.users.application.dto.AuthResponse;
import org.example.lv_be.module.users.application.interfaces.in.ILoginUseCase;
import org.example.lv_be.module.users.application.interfaces.out.IPasswordHasher;
import org.example.lv_be.module.users.application.interfaces.out.ITokenProvider;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.exception.AccountLockedException;
import org.example.lv_be.module.users.domain.exception.UserDomainException;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements ILoginUseCase {

    private final IUserRepository userRepository;
    private final IPasswordHasher passwordHasher;
    private final ITokenProvider tokenProvider;

    @Override
    public AuthResponse execute(AuthRequest request) {
        // 1. Tìm User theo số điện thoại
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new UserDomainException("Số điện thoại hoặc mật khẩu không chính xác!"));

        // 2. Xác thực mật khẩu
        if (!passwordHasher.matches(request.getPassword(), user.getPassword())) {
            throw new UserDomainException("Số điện thoại hoặc mật khẩu không chính xác!");
        }

        // 3. Gọi hàm từ Rich Domain Model để kiểm tra quyền truy cập
        if (!user.canAccessSystem()) {
            throw new AccountLockedException("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ Admin!");
        }

        // 4. Sinh Token và trả về
        String token = tokenProvider.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .fullName(user.getFullName()) // Lấy tên thay vì ID
                .role(user.getRole())
                .build();
    }
}