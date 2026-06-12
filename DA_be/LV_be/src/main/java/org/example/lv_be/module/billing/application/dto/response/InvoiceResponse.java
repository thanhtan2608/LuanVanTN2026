package org.example.lv_be.module.billing.application.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.lv_be.module.billing.domain.enums.InvoiceStatus;
import org.example.lv_be.module.billing.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class InvoiceResponse {
    private Long id;
    private Long bookingId;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private InvoiceStatus status;
    private LocalDateTime createdAt;
}