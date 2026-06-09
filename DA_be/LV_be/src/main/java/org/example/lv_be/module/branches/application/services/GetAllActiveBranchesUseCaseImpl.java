package org.example.lv_be.module.branches.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.branches.application.dto.BranchResponse;
import org.example.lv_be.module.branches.application.interfaces.IGetAllActiveBranchesUseCase;
import org.example.lv_be.module.branches.application.mappers.BranchMapper;
import org.example.lv_be.module.branches.domain.entity.Branch;
import org.example.lv_be.module.branches.domain.repository.IBranchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllActiveBranchesUseCaseImpl implements IGetAllActiveBranchesUseCase {

    private final IBranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponse> execute() {
        List<Branch> activeBranches = branchRepository.findAllActive();

        return activeBranches.stream()
                .map(branchMapper::toResponse)
                .toList();
    }
}