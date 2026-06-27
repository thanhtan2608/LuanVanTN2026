package org.example.lv_be.module.users.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.users.application.dto.UpdateStaffRequest;
import org.example.lv_be.module.users.application.interfaces.in.IUpdateStaffUseCase;
import org.example.lv_be.module.users.domain.entity.Employee;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.exception.UserDomainException;
import org.example.lv_be.module.users.domain.repository.IEmployeeRepository;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateStaffUseCaseImpl implements IUpdateStaffUseCase {

    private final IUserRepository userRepository;
    private final IEmployeeRepository employeeRepository;

    @Override
    @Transactional
    public void execute(Long staffId, UpdateStaffRequest request) {
        // 1. Lấy và cập nhật thông tin gốc (Bảng users)
        User user = userRepository.findById(staffId)
                .orElseThrow(() -> new UserDomainException("Không tìm thấy nhân viên với ID này!"));

        user.setFullName(request.getFullName());
        user.setRole(request.getRole());

        // Tận dụng Rich Domain Model để thay đổi trạng thái
        if (request.getIsActive()) {
            user.unlockAccount();
        } else {
            user.lockAccount();
        }

        userRepository.save(user);

        // 2. Lấy và cập nhật thông tin nghiệp vụ (Bảng employees)
        Employee employee = employeeRepository.findByUserId(staffId)
                .orElseThrow(() -> new UserDomainException("Không tìm thấy hồ sơ nhân sự của ID này!"));

        employee.setBranchId(request.getBranchId());
        employee.setBaseSalary(request.getBaseSalary());
        employee.setCommissionRate(request.getCommissionRate());

        employeeRepository.save(employee);
    }
}