package org.example.lv_be.module.billing.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.billing.domain.entity.InvoiceItem;
import org.example.lv_be.module.billing.domain.repository.IInvoiceItemRepository;
import org.example.lv_be.module.billing.infrastructure.persistence.entity.InvoiceItemJpaEntity;
import org.example.lv_be.module.billing.infrastructure.persistence.mapper.BillingPersistenceMapper;
import org.example.lv_be.module.billing.infrastructure.persistence.repository.InvoiceItemSpringJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InvoiceItemRepositoryImpl implements IInvoiceItemRepository {

    private final InvoiceItemSpringJpaRepository jpaRepository;
    private final BillingPersistenceMapper mapper;

    @Override
    public List<InvoiceItem> findByInvoiceId(Long invoiceId) {
        // Lấy dữ liệu từ DB (JPA Entity) và convert ngược lại thành Domain Entity
        return jpaRepository.findByInvoiceId(invoiceId).stream()
                .map(mapper::toItemDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void sourceSaveAll(List<InvoiceItem> items) {
        // Convert danh sách Domain Entity thành JPA Entity để Spring Data JPA lưu xuống DB
        List<InvoiceItemJpaEntity> jpaEntities = items.stream()
                .map(mapper::toItemJpaEntity)
                .collect(Collectors.toList());

        // Dùng saveAll để lưu toàn bộ danh sách chi tiết (dịch vụ, sản phẩm) trong 1 lần gọi DB (Batch Insert)
        jpaRepository.saveAll(jpaEntities);
    }
}