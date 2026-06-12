package org.example.lv_be.module.billing.infrastructure.persistence.repository;

import org.example.lv_be.module.billing.infrastructure.persistence.entity.InvoiceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceSpringJpaRepository extends JpaRepository<InvoiceJpaEntity, Long> {
    Optional<InvoiceJpaEntity> findByBookingId(Long bookingId);
}