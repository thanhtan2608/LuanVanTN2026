package org.example.lv_be.module.branches.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.exception.AppException;
import org.example.lv_be.module.branches.application.interfaces.IDeleteBranchUseCase;
import org.example.lv_be.module.branches.domain.entity.Branch;
import org.example.lv_be.module.branches.domain.repository.IBranchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteBranchUseCaseImpl implements IDeleteBranchUseCase {

    private final IBranchRepository branchRepository;

    @Override
    @Transactional
    public void execute(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy chi nhánh cần xóa!"));

        // Thực hiện hành vi Xóa mềm (Soft Delete)
        branch.setDeleted(true);
        branch.deactivate(); // Đã xóa thì tự động ngưng hoạt động

        branchRepository.save(branch);
    }
}