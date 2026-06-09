package org.example.lv_be.module.users.infrastructure.database.repository;
import org.example.lv_be.module.users.infrastructure.database.entity.OtpTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpTokenSpringJpaRepository extends JpaRepository<OtpTokenJpaEntity, Long> {
    Optional<OtpTokenJpaEntity> findByPhoneAndOtpCodeAndIsUsedFalse(String phone, String otpCode);
}