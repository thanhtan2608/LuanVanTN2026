package org.example.lv_be.module.billing.application.interfaces.in;

import org.example.lv_be.module.billing.application.dto.request.CreateInvoiceRequest;
import org.example.lv_be.module.billing.application.dto.response.InvoiceResponse;

public interface ICreateInvoiceUseCase {
    InvoiceResponse execute(CreateInvoiceRequest request);
}
