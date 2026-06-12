package org.example.lv_be.module.billing.infrastructure.persistence.mapper;

import org.example.lv_be.module.billing.domain.entity.Invoice;
import org.example.lv_be.module.billing.domain.entity.InvoiceItem;
import org.example.lv_be.module.billing.infrastructure.persistence.entity.InvoiceItemJpaEntity;
import org.example.lv_be.module.billing.infrastructure.persistence.entity.InvoiceJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillingPersistenceMapper {
    InvoiceJpaEntity toJpaEntity(Invoice domain);
    Invoice toDomainEntity(InvoiceJpaEntity jpaEntity);

    InvoiceItemJpaEntity toItemJpaEntity(InvoiceItem domain);
    InvoiceItem toItemDomainEntity(InvoiceItemJpaEntity jpaEntity);
}