package org.example.lv_be.module.billing.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentUrlResponse {
    private String payUrl; // Trả link này cho Frontend mở QR Code
}