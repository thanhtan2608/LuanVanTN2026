package org.example.lv_be.module.branches.infrastructure.persistence.mapper;

import org.example.lv_be.module.branches.domain.entity.Branch;
import org.example.lv_be.module.branches.infrastructure.persistence.entity.BranchJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BranchPersistenceMapper {

    BranchJpaEntity toJpaEntity(Branch domain);

    Branch toDomainEntity(BranchJpaEntity jpa);
}