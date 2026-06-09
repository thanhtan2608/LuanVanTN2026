package org.example.lv_be.module.users.infrastructure.database;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.users.domain.entity.OtpToken;
import org.example.lv_be.module.users.domain.repository.IOtpTokenRepository;
import org.example.lv_be.module.users.infrastructure.database.entity.OtpTokenJpaEntity;
import org.example.lv_be.module.users.infrastructure.database.repository.OtpTokenSpringJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OtpTokenRepositoryImpl implements IOtpTokenRepository {

    private final OtpTokenSpringJpaRepository jpaRepository;

    @Override
    public Optional<OtpToken> findByPhoneAndOtpCodeAndIsUsedFalse(String phone, String otpCode) {
        return jpaRepository.findByPhoneAndOtpCodeAndIsUsedFalse(phone, otpCode)
                .map(this::toDomainEntity);
    }

    @Override
    public OtpToken save(OtpToken otpToken) {
        OtpTokenJpaEntity jpaEntity = toJpaEntity(otpToken);
        OtpTokenJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return toDomainEntity(savedEntity);
    }

    // =========================================================
    // Các hàm Mapper chuyển đổi qua lại giữa Domain và JPA Entity
    // =========================================================

    private OtpToken toDomainEntity(OtpTokenJpaEntity entity) {
        if (entity == null) return null;
        return OtpToken.builder()
                .id(entity.getId())
                .phone(entity.getPhone())
                .otpCode(entity.getOtpCode())
                .expiresAt(entity.getExpiresAt())
                .isUsed(entity.isUsed())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private OtpTokenJpaEntity toJpaEntity(OtpToken domain) {
        if (domain == null) return null;
        return OtpTokenJpaEntity.builder()
                .id(domain.getId())
                .phone(domain.getPhone())
                .otpCode(domain.getOtpCode())
                .expiresAt(domain.getExpiresAt())
                .isUsed(domain.isUsed())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}