package org.example.lv_be.module.users.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.MemberTier;
import org.example.lv_be.common.enums.Role;
import org.example.lv_be.module.users.application.dto.AuthResponse;
import org.example.lv_be.module.users.application.dto.RegisterRequest;
import org.example.lv_be.module.users.application.interfaces.in.IRegisterUseCase;
import org.example.lv_be.module.users.application.interfaces.out.IPasswordHasher;
import org.example.lv_be.module.users.application.interfaces.out.ITokenProvider;
import org.example.lv_be.module.users.domain.entity.Customer;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.exception.UserDomainException;
import org.example.lv_be.module.users.domain.repository.ICustomerRepository;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUseCaseImpl implements IRegisterUseCase {

    private final IUserRepository userRepository;
    private final ICustomerRepository customerRepository;
    private final IPasswordHasher passwordHasher;
    private final ITokenProvider tokenProvider;

    @Override
    @Transactional
    public AuthResponse execute(RegisterRequest request) {
        // 1. Kiểm tra số điện thoại đã tồn tại chưa
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new UserDomainException("Số điện thoại này đã được đăng ký trong hệ thống!");
        }

        // 2. Khởi tạo đối tượng User (Tài khoản gốc)
        User newUser = User.builder()
                .phone(request.getPhone())
                .password(passwordHasher.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(Role.CUSTOMER)
                .isActive(true)
                .build();
        User savedUser = userRepository.save(newUser);

        // 3. Khởi tạo đối tượng Customer (Phần mở rộng)
        Customer newCustomer = Customer.builder()
                .userId(savedUser.getId())
                .points(0)
                .memberTier(MemberTier.NEW)
                .build();
        customerRepository.save(newCustomer);

        // 4. Cấp phát Token để tự động đăng nhập sau khi đăng ký
        String token = tokenProvider.generateToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .fullName(savedUser.getFullName()) // Lấy tên thay vì ID
                .role(savedUser.getRole())
                .build();
    }
}