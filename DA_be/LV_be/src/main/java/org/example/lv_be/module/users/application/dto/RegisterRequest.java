package org.example.lv_be.module.users.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterRequest extends AuthRequest {
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;
}