package org.example.lv_be.module.users.application.dto;

import jakarta.validation.constraints.*;
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
public class CreateStaffRequest {

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(03|05|07|08|09)\\d{8}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @NotNull(message = "Phân quyền không được để trống")
    private Role role; // Nên là MANAGER, RECEPTIONIST, hoặc STAFF

    // Các trường đặc thù của bảng employees
    private Long branchId; // Chi nhánh mà nhân viên này làm việc (có thể null nếu là Admin hệ thống)

    @NotNull(message = "Lương cơ bản không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Lương cơ bản không được là số âm")
    private BigDecimal baseSalary;

    @NotNull(message = "Tỉ lệ hoa hồng không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tỉ lệ hoa hồng không được nhỏ hơn 0%")
    @DecimalMax(value = "100.0", inclusive = true, message = "Tỉ lệ hoa hồng không được vượt quá 100%")
    private BigDecimal commissionRate;
}