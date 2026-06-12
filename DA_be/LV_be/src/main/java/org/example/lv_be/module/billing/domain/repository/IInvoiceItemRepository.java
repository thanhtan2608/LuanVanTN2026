package org.example.lv_be.module.billing.domain.repository;

import org.example.lv_be.module.billing.domain.entity.InvoiceItem;

import java.util.List;

public interface IInvoiceItemRepository {
    List<InvoiceItem> findByInvoiceId(Long invoiceId);
    void sourceSaveAll(List<InvoiceItem> items);
}