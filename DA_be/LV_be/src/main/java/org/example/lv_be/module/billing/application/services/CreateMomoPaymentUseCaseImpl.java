package org.example.lv_be.module.billing.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.billing.application.dto.response.PaymentUrlResponse;
import org.example.lv_be.module.billing.application.interfaces.in.ICreateMomoPaymentUseCase;
import org.example.lv_be.module.billing.application.interfaces.out.IMomoGatewayClient;
import org.example.lv_be.module.billing.domain.entity.Invoice;
import org.example.lv_be.module.billing.domain.enums.InvoiceStatus;
import org.example.lv_be.module.billing.domain.exception.BillingDomainException;
import org.example.lv_be.module.billing.domain.repository.IInvoiceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMomoPaymentUseCaseImpl implements ICreateMomoPaymentUseCase {
    private final IInvoiceRepository invoiceRepository;
    private final IMomoGatewayClient momoClient;

    @Override
    public PaymentUrlResponse execute(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new BillingDomainException("Không tìm thấy hóa đơn"));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BillingDomainException("Hóa đơn đã thanh toán, không thể tạo mã Momo mới.");
        }

        // Gọi sang Cổng Momo lấy Link
        String payUrl = momoClient.createPaymentUrl(invoice);
        return new PaymentUrlResponse(payUrl);
    }
}