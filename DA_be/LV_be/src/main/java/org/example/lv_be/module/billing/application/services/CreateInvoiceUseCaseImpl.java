package org.example.lv_be.module.billing.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.billing.application.dto.request.CreateInvoiceRequest;
import org.example.lv_be.module.billing.application.dto.response.InvoiceResponse;
import org.example.lv_be.module.billing.application.interfaces.in.ICreateInvoiceUseCase;
import org.example.lv_be.module.billing.application.mappers.InvoiceMapper;
import org.example.lv_be.module.billing.domain.entity.Invoice;
import org.example.lv_be.module.billing.domain.enums.InvoiceStatus;
import org.example.lv_be.module.billing.domain.repository.IInvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateInvoiceUseCaseImpl implements ICreateInvoiceUseCase {
    private final IInvoiceRepository invoiceRepository;
    private final InvoiceMapper mapper;

    @Override
    @Transactional
    public InvoiceResponse execute(CreateInvoiceRequest request) {
        Invoice invoice = mapper.toDomain(request);
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setCreatedAt(LocalDateTime.now());

        // 1. Map List Items và gán logic
        request.getItems().forEach(itemDto -> {
            invoice.getItems().add(mapper.toItemDomain(itemDto));
        });

        // 2. Kích hoạt logic Domain để tự cộng tiền các món
        invoice.calculateFinalAmount();

        // 3. Lưu xuống DB (Dữ liệu items sẽ được Cascade hoặc lưu qua Repo Item ở tầng Repo Impl)
        Invoice saved = invoiceRepository.sourceSave(invoice);
        return mapper.toResponse(saved);
    }
}