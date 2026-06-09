package org.example.lv_be.module.branches.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.exception.AppException;
import org.example.lv_be.module.branches.application.dto.BranchResponse;
import org.example.lv_be.module.branches.application.interfaces.IGetBranchByIdUseCase;
import org.example.lv_be.module.branches.application.mappers.BranchMapper;
import org.example.lv_be.module.branches.domain.entity.Branch;
import org.example.lv_be.module.branches.domain.repository.IBranchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetBranchByIdUseCaseImpl implements IGetBranchByIdUseCase {

    private final IBranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Override
    @Transactional(readOnly = true)
    public BranchResponse execute(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy chi nhánh với mã số: " + id));

        return branchMapper.toResponse(branch);
    }
}