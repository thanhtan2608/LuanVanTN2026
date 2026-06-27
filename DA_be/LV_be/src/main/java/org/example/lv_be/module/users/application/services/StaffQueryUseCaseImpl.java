package org.example.lv_be.module.users.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.users.application.dto.UserProfileResponse;
import org.example.lv_be.module.users.application.interfaces.in.IStaffQueryUseCase;
import org.example.lv_be.module.users.domain.entity.Employee;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.repository.IEmployeeRepository;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffQueryUseCaseImpl implements IStaffQueryUseCase {

    private final IEmployeeRepository employeeRepository;
    private final IUserRepository userRepository;

    @Override
    public List<UserProfileResponse> execute(Long branchId) {
        // 1. Lấy danh sách nhân viên thuộc chi nhánh này
        List<Employee> employees;
        if (branchId != null) {
            employees = employeeRepository.findByBranchId(branchId);
        } else {
            // Tạm thời nếu không truyền branchId thì trả về mảng rỗng (hoặc bạn có thể đổi logic thành findAll)
            return List.of();
        }

        // 2. Map dữ liệu sang DTO để trả về cho Frontend
        return employees.stream().map(emp -> {
            // Gọi sang bảng users để lấy thêm tên và số điện thoại
            User user = userRepository.findById(emp.getUserId()).orElse(null);

            return UserProfileResponse.builder()
                    .id(emp.getUserId())
                    .phone(user != null ? user.getPhone() : null)
                    .fullName(user != null ? user.getFullName() : null)
                    .role(user != null ? user.getRole() : null)
                    .isActive(user != null && user.isActive())
                    .branchId(emp.getBranchId())
                    .baseSalary(emp.getBaseSalary())
                    .commissionRate(emp.getCommissionRate())
                    .build();
        }).collect(Collectors.toList());
    }

    // =========================================
    // CODE CŨ BẠN ĐỂ Ở DƯỚI NÀY NẾU ĐANG CÓ LỖI ĐỎ THÌ TẠM RETURN NULL ĐỂ VƯỢT QUA
    // =========================================

    @Override
    public BigDecimal getBaseSalary(Long staffId) {
        return null;
    }

    @Override
    public LocalTime getShiftStartTime(Long staffId) {
        return null;
    }

    @Override
    public Map<Long, String> getAllActiveStaffs() {
        return null;
    }
}