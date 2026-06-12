package org.example.lv_be.module.billing.infrastructure.external.momo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "momo.payment") // Cập nhật lại prefix cho đúng
@Getter
@Setter
public class MomoConfig {

    // Tự động map với momo.payment.partner-code
    private String partnerCode;

    // Tự động map với momo.payment.access-key
    private String accessKey;

    // Tự động map với momo.payment.secret-key
    private String secretKey;

    // Đổi apiUrl -> endpoint để map với momo.payment.endpoint
    private String endpoint;

    // Đổi returnUrl -> redirectUrl để map với momo.payment.redirect-url
    private String redirectUrl;

    // Đổi notifyUrl -> ipnUrl để map với momo.payment.ipn-url
    private String ipnUrl;
}