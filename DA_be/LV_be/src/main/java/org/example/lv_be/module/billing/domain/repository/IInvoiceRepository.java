package org.example.lv_be.module.billing.domain.repository;

import org.example.lv_be.module.billing.domain.entity.Invoice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IInvoiceRepository {
    Optional<Invoice> findById(Long id);
    Optional<Invoice> findByCode(String code);
    Optional<Invoice> findByBookingId(Long bookingId); // Tìm hóa đơn theo đơn đặt lịch
    List<Invoice> findByCustomerId(Long customerId);
    List<Invoice> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate); // Phục vụ báo cáo cuối ngày

    Invoice sourceSave(Invoice invoice);
}