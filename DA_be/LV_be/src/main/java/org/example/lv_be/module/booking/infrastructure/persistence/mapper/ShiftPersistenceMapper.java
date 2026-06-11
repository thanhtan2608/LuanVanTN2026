package org.example.lv_be.module.booking.infrastructure.persistence.mapper;

import org.example.lv_be.module.booking.domain.entity.Shift;
import org.example.lv_be.module.booking.infrastructure.persistence.entity.ShiftJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShiftPersistenceMapper {
    ShiftJpaEntity toJpaEntity(Shift domain);
    Shift toDomainEntity(ShiftJpaEntity jpa);
}