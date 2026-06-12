package org.example.lv_be.module.billing.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.billing.domain.entity.Invoice;
import org.example.lv_be.module.billing.domain.repository.IInvoiceRepository;
import org.example.lv_be.module.billing.infrastructure.persistence.entity.InvoiceJpaEntity;
import org.example.lv_be.module.billing.infrastructure.persistence.mapper.BillingPersistenceMapper;
import org.example.lv_be.module.billing.infrastructure.persistence.repository.InvoiceSpringJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InvoiceRepositoryImpl implements IInvoiceRepository {
    private final InvoiceSpringJpaRepository jpaRepository;
    private final BillingPersistenceMapper mapper;

    @Override
    public Optional<Invoice> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public Optional<Invoice> findByCode(String code) {
        return Optional.empty(); // Bảng SQL hiện tại chưa có cột code, có thể bổ sung sau
    }

    @Override
    public Optional<Invoice> findByBookingId(Long bookingId) {
        return jpaRepository.findByBookingId(bookingId).map(mapper::toDomainEntity);
    }

    @Override
    public List<Invoice> findByCustomerId(Long customerId) {
        return List.of(); // Cài đặt query thêm trong SpringJpaRepo nếu cần
    }

    @Override
    public List<Invoice> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return List.of(); // Cài đặt query thêm trong SpringJpaRepo nếu cần
    }

    @Override
    public Invoice sourceSave(Invoice invoice) {
        InvoiceJpaEntity jpaEntity = mapper.toJpaEntity(invoice);
        return mapper.toDomainEntity(jpaRepository.save(jpaEntity));
    }
}