package org.example.lv_be.module.users.infrastructure.database.repository;

import org.example.lv_be.module.users.infrastructure.database.entity.OtpTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpTokenSpringJpaRepository extends JpaRepository<OtpTokenJpaEntity, Long> {

    // Spring Data tự động gen query tìm mã OTP chưa sử dụng
    Optional<OtpTokenJpaEntity> findByPhoneAndOtpCodeAndIsUsedFalse(String phone, String otpCode);

    // 🌟 Bổ sung lệnh UPDATE để vô hiệu hóa toàn bộ OTP cũ của 1 số điện thoại
    @Modifying
    @Query("UPDATE OtpTokenJpaEntity o SET o.isUsed = true WHERE o.phone = :phone AND o.isUsed = false")
    void invalidateAllByPhone(@Param("phone") String phone);
}