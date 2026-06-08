package org.example.lv_be.module.users.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(03|05|07|08|09)\\d{8}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}