package org.example.lv_be.module.users.domain.repository;

import org.example.lv_be.module.users.domain.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeRepository {
    Employee save(Employee employee);

    // Tìm thông tin nhân viên/thợ dựa trên ID của bảng Users
    Optional<Employee> findByUserId(Long userId);

    // Dùng để lấy danh sách thợ của một chi nhánh cụ thể cho màn hình đặt lịch
    List<Employee> findByBranchId(Long branchId);
}