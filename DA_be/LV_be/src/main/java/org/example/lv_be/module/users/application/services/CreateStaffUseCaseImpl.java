package org.example.lv_be.module.users.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.users.application.dto.CreateStaffRequest;
import org.example.lv_be.module.users.application.interfaces.in.ICreateStaffUseCase;
import org.example.lv_be.module.users.application.interfaces.out.IPasswordHasher;
import org.example.lv_be.module.users.domain.entity.Employee;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.exception.UserDomainException;
import org.example.lv_be.module.users.domain.repository.IEmployeeRepository;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateStaffUseCaseImpl implements ICreateStaffUseCase {

    private final IUserRepository userRepository;
    private final IEmployeeRepository employeeRepository;
    private final IPasswordHasher passwordHasher;

    @Override
    @Transactional
    public void execute(CreateStaffRequest request) {
        // 1. Kiểm tra số điện thoại
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new UserDomainException("Số điện thoại này đã được sử dụng!");
        }

        // 2. Tạo User Gốc
        User newUser = User.builder()
                .phone(request.getPhone())
                .password(passwordHasher.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(request.getRole()) // Role truyền lên (STAFF, MANAGER, RECEPTIONIST)
                .isActive(true)
                .build();
        User savedUser = userRepository.save(newUser);

        // 3. Tạo Employee (Phần mở rộng thông tin nhân sự)
        Employee newEmployee = Employee.builder()
                .userId(savedUser.getId())
                .branchId(request.getBranchId())
                .baseSalary(request.getBaseSalary())
                .commissionRate(request.getCommissionRate())
                .build();
        employeeRepository.save(newEmployee);
    }
}