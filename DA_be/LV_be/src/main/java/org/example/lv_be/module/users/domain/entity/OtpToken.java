package org.example.lv_be.module.users.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.lv_be.module.users.domain.exception.InvalidOtpException;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OtpToken {
    private Long id;
    private String phone;
    private String otpCode;
    private LocalDateTime expiresAt;
    private boolean isUsed;
    private LocalDateTime createdAt;

    public OtpToken() {
        this.createdAt = LocalDateTime.now();
        this.isUsed = false;
    }

    // Logic nghiệp vụ: Token có hợp lệ không?
    public boolean isValid() {
        return !isUsed && expiresAt.isAfter(LocalDateTime.now());
    }

    public void markAsUsed() {
        this.isUsed = true;
    }
    public void validate() {
        if (this.isUsed) {
            throw new InvalidOtpException("Mã OTP này đã được sử dụng!");
        }
        if (this.expiresAt.isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("Mã OTP đã hết hạn!");
        }
    }
}