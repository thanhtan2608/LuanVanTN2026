package org.example.lv_be.module.catalog.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.domain.entity.ServiceItem;
import org.example.lv_be.module.catalog.domain.repository.IServiceRepository;
import org.example.lv_be.module.catalog.infrastructure.persistence.entity.ServiceItemJpaEntity;
import org.example.lv_be.module.catalog.infrastructure.persistence.mapper.ServiceItemPersistenceMapper;
import org.example.lv_be.module.catalog.infrastructure.persistence.repository.ServiceItemSpringJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ServiceItemRepositoryImpl implements IServiceRepository {

    private final ServiceItemSpringJpaRepository jpaRepository;
    private final ServiceItemPersistenceMapper mapper;

    @Override
    public Optional<ServiceItem> findById(Long id) {
        return jpaRepository.findById(id)
                .filter(jpa -> !jpa.isDeleted())
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<ServiceItem> findByCategoryIdAndActiveTrue(Long categoryId) {
        return jpaRepository.findByCategoryIdAndActiveTrueAndDeletedFalse(categoryId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<ServiceItem> findAllActive() {
        return jpaRepository.findByActiveTrueAndDeletedFalse().stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public ServiceItem sourceSave(ServiceItem serviceItem) {
        ServiceItemJpaEntity jpa = mapper.toJpaEntity(serviceItem);
        ServiceItemJpaEntity saved = jpaRepository.save(jpa);
        return mapper.toDomainEntity(saved);
    }
}