package org.example.lv_be.module.hairstyles.infrastructure.persistence.mapper;

import org.example.lv_be.module.hairstyles.domain.entity.Hairstyle;
import org.example.lv_be.module.hairstyles.infrastructure.persistence.entity.HairstyleJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HairstylePersistenceMapper {
    HairstyleJpaEntity toJpaEntity(Hairstyle domain);
    Hairstyle toDomainEntity(HairstyleJpaEntity jpa);
}