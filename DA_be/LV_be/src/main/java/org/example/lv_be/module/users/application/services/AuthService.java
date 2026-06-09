package org.example.lv_be.module.users.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.Role;
import org.example.lv_be.core.exception.AppException;
import org.example.lv_be.core.security.ITokenBlacklistRepository;
import org.example.lv_be.module.users.application.dto.*;
import org.example.lv_be.module.users.application.interfaces.IPasswordHasher;
import org.example.lv_be.module.users.application.interfaces.ISmsService;
import org.example.lv_be.module.users.application.interfaces.ITokenProvider;
import org.example.lv_be.module.users.application.mappers.UserMapper;
import org.example.lv_be.module.users.domain.entity.OtpToken;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.repository.IOtpTokenRepository;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    // 100% Giao tiếp qua Interface (Dependency Inversion)
    private final IUserRepository userRepository;
    private final IPasswordHasher passwordHasher;
    private final ITokenProvider tokenProvider;
    private final UserMapper userMapper;
    private final IOtpTokenRepository otpTokenRepository;
    private final ISmsService smsService;
    private final ITokenBlacklistRepository tokenBlacklistRepository;

    @Transactional
    public AuthResponse registerCustomer(RegisterRequest request) {
        // 1. Kiểm tra SĐT
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new AppException(HttpStatus.CONFLICT, "Số điện thoại đã được đăng ký!");
        }

        // 2. Chuyển DTO thành Domain Entity
        User newUser = userMapper.toDomainEntity(request);

        // 3. Băm mật khẩu (Gọi Interface của mình, không quan tâm là BCrypt hay MD5)
        newUser.setPassword(passwordHasher.encode(request.getPassword()));
        newUser.setRole(Role.CUSTOMER);

        // 4. Lưu dữ liệu
        User savedUser = userRepository.save(newUser);

        // 5. Sinh Token
        String jwtToken = tokenProvider.generateToken(savedUser);

        return AuthResponse.builder()
                .token(jwtToken)
                .fullName(savedUser.getFullName())
                .role(savedUser.getRole())
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        // 1. Lấy User từ DB lên
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Số điện thoại không tồn tại!"));

        // 2. Tự kiểm tra mật khẩu bằng Interface
        if (!passwordHasher.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Mật khẩu không chính xác!");
        }

        // 3. Kiểm tra trạng thái tài khoản
        if (!user.isActive()) {
            throw new AppException(HttpStatus.FORBIDDEN, "Tài khoản của bạn đã bị khóa!");
        }

        // 4. Sinh Token
        String jwtToken = tokenProvider.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }
    public UserProfileResponse getProfile(String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin tài khoản này."));

        // Sử dụng MapStruct UserMapper đã viết ở các bước trước để biến đổi Domain -> DTO
        return userMapper.toProfileResponse(user);
    }
    // 1. ĐỔI MẬT KHẨU (Tất cả Role)
    @Transactional
    public void changePassword(String phone, ChangePasswordRequest request) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng!"));

        // Kiểm tra mật khẩu cũ
        if (!passwordHasher.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Mật khẩu cũ không chính xác!");
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordHasher.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // 2. ĐĂNG XUẤT (Tất cả Role)
    public void logout(String token) {
        // Cắt bỏ chữ "Bearer " nếu Frontend lỡ truyền thừa
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        tokenBlacklistRepository.blacklistToken(token);
    }

    // 3. YÊU CẦU QUÊN MẬT KHẨU (Chỉ Customer)
    @Transactional
    public void requestForgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Số điện thoại chưa được đăng ký!"));

        if (user.getRole() != Role.CUSTOMER) {
            throw new AppException(HttpStatus.FORBIDDEN, "Chức năng này chỉ dành cho Khách hàng!");
        }

        // Sinh mã OTP 6 số
        String otp = String.format("%06d", new java.util.Random().nextInt(999999));

        // Lưu OTP vào Database (Hết hạn sau 5 phút)
        OtpToken otpToken = new OtpToken();
        otpToken.setPhone(user.getPhone());
        otpToken.setOtpCode(otp);
        otpToken.setExpiresAt(java.time.LocalDateTime.now().plusMinutes(5));
        otpTokenRepository.save(otpToken);

        // Gửi SMS
        smsService.sendOtpSms(user.getPhone(), otp);
    }

    // 4. ĐẶT LẠI MẬT KHẨU BẰNG OTP
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // Tìm OTP còn hạn và chưa sử dụng
        OtpToken otpToken = otpTokenRepository.findByPhoneAndOtpCodeAndIsUsedFalse(request.getPhone(), request.getOtpCode())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Mã OTP không hợp lệ hoặc đã hết hạn!"));

        // Validate logic của Domain (Hết hạn)
        if (!otpToken.isValid()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Mã OTP đã hết hạn sử dụng!");
        }

        // Lấy User lên và đổi mật khẩu
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng!"));
        user.setPassword(passwordHasher.encode(request.getNewPassword()));
        userRepository.save(user);

        // Đánh dấu OTP đã dùng
        otpToken.markAsUsed();
        otpTokenRepository.save(otpToken);
    }
}