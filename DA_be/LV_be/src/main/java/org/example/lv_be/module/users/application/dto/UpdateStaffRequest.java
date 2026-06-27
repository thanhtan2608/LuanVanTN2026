package org.example.lv_be.module.users.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lv_be.common.enums.Role;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStaffRequest {

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @NotNull(message = "Phân quyền không được để trống")
    private Role role; // Admin có thể muốn thăng chức từ STAFF lên MANAGER

    private Long branchId; // Có thể null (Ví dụ: Chuyển một quản lý chi nhánh lên làm quản lý tổng)

    @NotNull(message = "Trạng thái hoạt động không được để trống")
    private Boolean isActive; // Cho phép Admin khóa/mở khóa nhân viên

    @NotNull(message = "Lương cơ bản không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Lương cơ bản không được là số âm")
    private BigDecimal baseSalary;

    @NotNull(message = "Tỉ lệ hoa hồng không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tỉ lệ hoa hồng không được nhỏ hơn 0%")
    @DecimalMax(value = "100.0", inclusive = true, message = "Tỉ lệ hoa hồng không được vượt quá 100%")
    private BigDecimal commissionRate;
}