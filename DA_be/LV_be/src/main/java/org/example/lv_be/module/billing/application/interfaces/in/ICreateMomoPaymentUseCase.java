package org.example.lv_be.module.billing.application.interfaces.in;

import org.example.lv_be.module.billing.application.dto.response.PaymentUrlResponse;

public interface ICreateMomoPaymentUseCase {
    PaymentUrlResponse execute(Long invoiceId);
}
