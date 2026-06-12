package org.example.lv_be.module.billing.application.mappers;

import org.example.lv_be.module.billing.application.dto.request.CreateInvoiceRequest;
import org.example.lv_be.module.billing.application.dto.response.InvoiceResponse;
import org.example.lv_be.module.billing.domain.entity.Invoice;
import org.example.lv_be.module.billing.domain.entity.InvoiceItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    Invoice toDomain(CreateInvoiceRequest request);
    InvoiceItem toItemDomain(CreateInvoiceRequest.InvoiceItemRequest itemRequest);
    InvoiceResponse toResponse(Invoice invoice);
}