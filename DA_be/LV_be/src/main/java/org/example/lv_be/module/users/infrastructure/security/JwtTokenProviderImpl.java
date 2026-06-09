package org.example.lv_be.module.users.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.security.JwtUtil;
import org.example.lv_be.module.users.application.interfaces.ITokenProvider;
import org.example.lv_be.module.users.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements ITokenProvider {

    private final JwtUtil jwtUtil;

    @Override
    public String generateToken(User user) {
        // Gọi CustomUserDetails (Đã làm phẳng ở bước trước)
        CustomUserDetails userDetails = new CustomUserDetails(user);
        return jwtUtil.generateToken(userDetails);
    }
}