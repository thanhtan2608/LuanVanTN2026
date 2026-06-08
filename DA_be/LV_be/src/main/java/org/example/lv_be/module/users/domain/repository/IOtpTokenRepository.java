package org.example.lv_be.module.users.domain.repository;

import org.example.lv_be.module.users.domain.entity.OtpToken;
import java.util.Optional;

public interface IOtpTokenRepository {

    Optional<OtpToken> findByPhoneAndOtpCodeAndIsUsedFalse(String phone, String otpCode);

    OtpToken save(OtpToken otpToken);
}