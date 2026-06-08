package org.example.lv_be.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Cho phép phân quyền bằng Anotation @PreAuthorize trên Controller
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF vì chúng ta dùng Token
                .cors(cors -> cors.configure(http))    // Bật CORS (Đã config ở file CorsConfig)
                .authorizeHttpRequests(auth -> auth
                        // 1. Các API cho phép gọi thoải mái không cần Token (Public)
                        .requestMatchers(
                                "/api/v1/auth/**",      // Login, Register, OTP
                                "/api/v1/public/**",    // Xem danh sách dịch vụ, thợ cắt
                                "/v3/api-docs/**",      // Tài liệu Swagger
                                "/swagger-ui/**"
                        ).permitAll()

                        // 2. Các API còn lại bắt buộc phải có Token hợp lệ
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Không lưu session trên server
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}