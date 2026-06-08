package org.example.lv_be.module.users.application.dto;

import lombok.Builder;
import lombok.Data;
import org.example.lv_be.common.enums.Role;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String fullName;
    private Role role;
}