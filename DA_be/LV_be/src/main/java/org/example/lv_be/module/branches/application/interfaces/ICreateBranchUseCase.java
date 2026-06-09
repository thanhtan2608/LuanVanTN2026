package org.example.lv_be.module.branches.application.interfaces;

import org.example.lv_be.module.branches.application.dto.CreateBranchRequest;
import org.example.lv_be.module.branches.application.dto.BranchResponse;

public interface ICreateBranchUseCase {
    BranchResponse execute(CreateBranchRequest request);
}