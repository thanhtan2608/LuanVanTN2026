package org.example.lv_be.module.billing.infrastructure.persistence.repository;

import org.example.lv_be.module.billing.infrastructure.persistence.entity.InvoiceItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceItemSpringJpaRepository extends JpaRepository<InvoiceItemJpaEntity, Long> {
    List<InvoiceItemJpaEntity> findByInvoiceId(Long invoiceId);
}