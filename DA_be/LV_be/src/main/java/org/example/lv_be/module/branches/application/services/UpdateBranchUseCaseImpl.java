package org.example.lv_be.module.branches.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.exception.AppException;
import org.example.lv_be.module.branches.application.dto.BranchResponse;
import org.example.lv_be.module.branches.application.dto.UpdateBranchRequest;
import org.example.lv_be.module.branches.application.interfaces.IUpdateBranchUseCase;
import org.example.lv_be.module.branches.application.mappers.BranchMapper;
import org.example.lv_be.module.branches.domain.entity.Branch;
import org.example.lv_be.module.branches.domain.repository.IBranchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateBranchUseCaseImpl implements IUpdateBranchUseCase {

    private final IBranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Override
    @Transactional
    public BranchResponse execute(Long id, UpdateBranchRequest request) {
        // 1. Tìm kiếm thực thể hiện tại
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy chi nhánh yêu cầu cập nhật!"));

        // 2. Kiểm tra ràng buộc thời gian
        if (!request.getOpenTime().isBefore(request.getCloseTime())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Giờ đóng cửa phải sau giờ mở cửa!");
        }

        // 3. Kiểm tra trùng tên với các chi nhánh khác (trừ chính nó)
        if (branchRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(HttpStatus.CONFLICT, "Tên chi nhánh mới đã bị trùng lập!");
        }

        // 4. Đồng bộ hóa dữ liệu thay đổi vào thực thể Domain
        branchMapper.updateEntityFromDto(request, branch);

        // 5. Đồng bộ hóa trạng thái hoạt động thông qua hành vi của Domain
        if (request.getIsActive()) {
            branch.activate();
        } else {
            branch.deactivate();
        }

        Branch updatedBranch = branchRepository.save(branch);
        return branchMapper.toResponse(updatedBranch);
    }
}