package org.example.lv_be.module.billing.application.interfaces.out;

import org.example.lv_be.module.billing.domain.entity.Invoice;

public interface IMomoGatewayClient {
    // Gọi sang API của Momo để lấy link thanh toán
    String createPaymentUrl(Invoice invoice);

    // Thuật toán mã hóa HMAC SHA256 kiểm tra request có đúng của Momo gửi không
    boolean verifySignature(String signature, String rawHashData);

    // Xác thực IPN Callback
    boolean verifyIpnSignature(org.example.lv_be.module.billing.application.dto.request.MomoIpnCallbackRequest request);
}
