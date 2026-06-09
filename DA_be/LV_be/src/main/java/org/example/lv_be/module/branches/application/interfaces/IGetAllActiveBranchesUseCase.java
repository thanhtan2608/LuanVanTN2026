package org.example.lv_be.module.branches.application.interfaces;

import org.example.lv_be.module.branches.application.dto.BranchResponse;
import java.util.List;

public interface IGetAllActiveBranchesUseCase {
    List<BranchResponse> execute();
}