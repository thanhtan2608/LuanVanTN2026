package org.example.lv_be.module.billing.infrastructure.external.momo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lv_be.module.billing.application.dto.request.MomoIpnCallbackRequest;
import org.example.lv_be.module.billing.application.interfaces.out.IMomoGatewayClient;
import org.example.lv_be.module.billing.domain.entity.Invoice;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MomoGatewayClientImpl implements IMomoGatewayClient {

    private final MomoConfig momoConfig;
    private final RestTemplate restTemplate; // Inject RestTemplate để call API

    @Override
    public String createPaymentUrl(Invoice invoice) {
        try {
            // Cộng thêm currentTimeMillis để tránh lỗi trùng mã đơn hàng khi test liên tục
            String orderId = "INV-" + invoice.getId() + "-" + System.currentTimeMillis();
            String requestId = UUID.randomUUID().toString();

            // Ép kiểu về chuỗi số nguyên, MoMo không nhận số thập phân
            String amount = String.valueOf(invoice.getTotalAmount().longValue());
            String orderInfo = "Thanh toan hoa don " + orderId;
            String requestType = "captureWallet";
            String extraData = "";

            // 1. Tạo chuỗi dữ liệu thô (Raw Signature) theo thứ tự alphabet
            String rawSignature = "accessKey=" + momoConfig.getAccessKey() +
                    "&amount=" + amount +
                    "&extraData=" + extraData +
                    "&ipnUrl=" + momoConfig.getIpnUrl() +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + momoConfig.getPartnerCode() +
                    "&redirectUrl=" + momoConfig.getRedirectUrl() +
                    "&requestId=" + requestId +
                    "&requestType=" + requestType;

            // 2. Ký signature
            String signature = generateHmacSHA256(rawSignature, momoConfig.getSecretKey());

            // 3. Xây dựng Request Body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", momoConfig.getPartnerCode());
            requestBody.put("partnerName", "TravelGood");
            requestBody.put("storeId", "TravelGoodStore");
            requestBody.put("requestId", requestId);
            requestBody.put("amount", amount);
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", momoConfig.getRedirectUrl());
            requestBody.put("ipnUrl", momoConfig.getIpnUrl());
            requestBody.put("lang", "vi");
            requestBody.put("extraData", extraData);
            requestBody.put("requestType", requestType);
            requestBody.put("signature", signature);

            // 4. Gửi request tới MoMo
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            Map<String, Object> response = restTemplate.postForObject(momoConfig.getEndpoint(), entity, Map.class);

            // 5. Bóc tách payUrl từ JSON response
            if (response != null && response.containsKey("payUrl")) {
                return (String) response.get("payUrl");
            } else {
                log.error("MoMo API Error Response: {}", response);
                throw new RuntimeException("Không tìm thấy payUrl trong phản hồi từ hệ thống thanh toán");
            }

        } catch (Exception e) {
            log.error("Lỗi khi tạo payment URL MoMo", e);
            throw new RuntimeException("Lỗi tích hợp MoMo: " + e.getMessage());
        }
    }

    @Override
    public boolean verifySignature(String signature, String rawHashData) {
        String computedHash = generateHmacSHA256(rawHashData, momoConfig.getSecretKey());
        return computedHash.equals(signature);
    }

    @Override
    public boolean verifyIpnSignature(MomoIpnCallbackRequest request) {
        // Build raw signature according to MoMo IPN spec
        String rawHashData = "accessKey=" + momoConfig.getAccessKey() +
                "&amount=" + request.getAmount() +
                "&extraData=" + request.getExtraData() +
                "&message=" + request.getMessage() +
                "&orderId=" + request.getOrderId() +
                "&orderInfo=" + request.getOrderInfo() +
                "&orderType=" + request.getOrderType() +
                "&partnerCode=" + request.getPartnerCode() +
                "&payType=" + request.getPayType() +
                "&requestId=" + request.getRequestId() +
                "&responseTime=" + request.getResponseTime() +
                "&resultCode=" + request.getResultCode() +
                "&transId=" + request.getTransId();

        String computedHash = generateHmacSHA256(rawHashData, momoConfig.getSecretKey());
        return computedHash.equals(request.getSignature());
    }

    /**
     * Hàm dùng chung để tạo chữ ký HMAC_SHA256 cho cả việc Gửi Request và Verify IPN
     */
    private String generateHmacSHA256(String data, String secretKey) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] bytes = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Dùng StringBuilder thay cho Formatter để tối ưu bộ nhớ và tránh rò rỉ tài nguyên
            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("Lỗi mã hóa dữ liệu", e);
            throw new RuntimeException("Lỗi hệ thống khi tạo chữ ký bảo mật");
        }
    }
}