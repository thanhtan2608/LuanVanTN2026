package org.example.lv_be.module.branches.application.interfaces;

import org.example.lv_be.module.branches.application.dto.BranchResponse;

public interface IGetBranchByIdUseCase {
    BranchResponse execute(Long id);
}