package org.example.lv_be.module.billing.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MomoIpnCallbackRequest {
    private String partnerCode;
    private String orderId;       // Mã hóa đơn của hệ thống (VD: INV-123)
    private String requestId;
    private Long amount;
    private String orderInfo;
    private String orderType;
    private Long transId;
    private Integer resultCode;   // 0 là thành công, khác 0 là thất bại
    private String message;
    private String payType;
    private String responseTime;
    private String extraData;
    private String signature;     // Chữ ký điện tử để verify
}