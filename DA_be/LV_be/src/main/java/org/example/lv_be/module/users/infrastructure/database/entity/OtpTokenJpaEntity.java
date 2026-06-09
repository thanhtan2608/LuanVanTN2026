package org.example.lv_be.module.users.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpTokenJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String phone;
    @Column(name = "otp_code", nullable = false, length = 100)
    private String otpCode;
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    private boolean isUsed;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}