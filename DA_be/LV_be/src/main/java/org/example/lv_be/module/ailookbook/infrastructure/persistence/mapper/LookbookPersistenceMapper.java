package org.example.lv_be.module.ailookbook.infrastructure.persistence.mapper;

import org.example.lv_be.module.ailookbook.domain.entity.LookbookItem;
import org.example.lv_be.module.ailookbook.infrastructure.persistence.entity.LookbookJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LookbookPersistenceMapper {

    LookbookJpaEntity toJpaEntity(LookbookItem domain);

    LookbookItem toDomainEntity(LookbookJpaEntity jpa);
}