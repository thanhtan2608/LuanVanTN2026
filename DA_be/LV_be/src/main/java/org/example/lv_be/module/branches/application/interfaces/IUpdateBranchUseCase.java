package org.example.lv_be.module.branches.application.interfaces;

import org.example.lv_be.module.branches.application.dto.BranchResponse;
import org.example.lv_be.module.branches.application.dto.UpdateBranchRequest;

public interface IUpdateBranchUseCase {
    BranchResponse execute(Long id, UpdateBranchRequest request);
}