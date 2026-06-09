package org.example.lv_be.module.ailookbook.infrastructure.persistence.mapper;

import org.example.lv_be.module.ailookbook.domain.entity.UserAiStyle;
import org.example.lv_be.module.ailookbook.infrastructure.persistence.entity.UserAiStyleJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAiStylePersistenceMapper {

    UserAiStyleJpaEntity toJpaEntity(UserAiStyle domain);

    UserAiStyle toDomainEntity(UserAiStyleJpaEntity jpa);
}