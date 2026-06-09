package org.example.lv_be.module.branches.application.mappers;

import org.example.lv_be.module.branches.application.dto.CreateBranchRequest;
import org.example.lv_be.module.branches.application.dto.BranchResponse;
import org.example.lv_be.module.branches.application.dto.UpdateBranchRequest;
import org.example.lv_be.module.branches.domain.entity.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    Branch toEntity(CreateBranchRequest request);

    BranchResponse toResponse(Branch branch);

    void updateEntityFromDto(UpdateBranchRequest dto, @MappingTarget Branch branch);
}