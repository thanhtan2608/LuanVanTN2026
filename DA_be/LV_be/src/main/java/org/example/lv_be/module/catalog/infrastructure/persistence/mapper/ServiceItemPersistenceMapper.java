package org.example.lv_be.module.catalog.infrastructure.persistence.mapper;

import org.example.lv_be.module.catalog.domain.entity.ServiceItem;
import org.example.lv_be.module.catalog.infrastructure.persistence.entity.ServiceItemJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceItemPersistenceMapper {
    ServiceItemJpaEntity toJpaEntity(ServiceItem domain);
    ServiceItem toDomainEntity(ServiceItemJpaEntity jpa);
}