package org.example.lv_be.module.branches.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.exception.AppException;
import org.example.lv_be.module.branches.application.dto.CreateBranchRequest;
import org.example.lv_be.module.branches.application.dto.BranchResponse;
import org.example.lv_be.module.branches.application.interfaces.ICreateBranchUseCase;
import org.example.lv_be.module.branches.application.mappers.BranchMapper;
import org.example.lv_be.module.branches.domain.entity.Branch;
import org.example.lv_be.module.branches.domain.repository.IBranchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateBranchUseCaseImpl implements ICreateBranchUseCase {

    private final IBranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Override
    @Transactional
    public BranchResponse execute(CreateBranchRequest request) {
        // 1. Kiểm tra ràng buộc thời gian
        if (!request.getOpenTime().isBefore(request.getCloseTime())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Giờ đóng cửa phải sau giờ mở cửa!");
        }

        // 2. Tìm kiếm chi nhánh (Bao gồm cả dữ liệu đã xóa mềm)
        Optional<Branch> existingBranchOpt = branchRepository.findByNameIncludingDeleted(request.getName());

        Branch branchToSave;

        if (existingBranchOpt.isPresent()) {
            branchToSave = existingBranchOpt.get();

            if (!branchToSave.isDeleted()) {
                // Trường hợp 1: Chi nhánh đang tồn tại và đang hoạt động bình thường
                throw new AppException(HttpStatus.CONFLICT, "Tên chi nhánh này đã tồn tại trên hệ thống!");
            } else {
                // Trường hợp 2: Chi nhánh đã từng tồn tại nhưng đã bị xóa mềm trước đó -> Tiến hành PHỤC HỒI
                branchToSave.setDeleted(false);
                branchToSave.setActive(true);
            }
        } else {
            // Trường hợp 3: Tên chi nhánh hoàn toàn mới -> Khởi tạo mới bản ghi
            branchToSave = new Branch();
            branchToSave.setName(request.getName());
            branchToSave.setCreatedAt(LocalDateTime.now());
            branchToSave.setActive(true);
            branchToSave.setDeleted(false);
        }

        // 3. Cập nhật đè dữ liệu mới lên (Dùng chung cho cả luồng Tạo mới và Phục hồi)
        branchToSave.setAddress(request.getAddress());
        branchToSave.setPhone(request.getPhone());
        branchToSave.setOpenTime(request.getOpenTime());
        branchToSave.setCloseTime(request.getCloseTime());

        // 4. Lưu xuống cơ sở dữ liệu
        Branch savedBranch = branchRepository.save(branchToSave);
        return branchMapper.toResponse(savedBranch);
    }
}