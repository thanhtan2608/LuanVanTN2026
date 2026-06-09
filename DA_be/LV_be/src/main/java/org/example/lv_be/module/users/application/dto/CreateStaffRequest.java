package org.example.lv_be.module.users.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.lv_be.common.enums.Role;

@Data
public class CreateStaffRequest {
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(03|05|07|08|09)\\d{8}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @NotNull(message = "Phân quyền không được để trống")
    private Role role; // Admin sẽ truyền lên là MANAGER hoặc STAFF

    private Long branchId; // Chi nhánh mà nhân viên này làm việc (có thể null nếu chưa phân)
}