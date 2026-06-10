package org.example.lv_be.module.hairstyles.infrastructure.persistence.mapper;

import org.example.lv_be.module.hairstyles.domain.entity.HairstyleService;
import org.example.lv_be.module.hairstyles.infrastructure.persistence.entity.HairstyleServiceJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HairstyleServicePersistenceMapper {
    HairstyleServiceJpaEntity toJpaEntity(HairstyleService domain);
    HairstyleService toDomainEntity(HairstyleServiceJpaEntity jpa);
}