package org.example.lv_be.module.users.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateStaffRequest {
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    private Long branchId;

    private Boolean isActive; // Cho phép Admin khóa/mở khóa nhân viên
}