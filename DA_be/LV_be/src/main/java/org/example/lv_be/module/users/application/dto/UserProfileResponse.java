package org.example.lv_be.module.users.application.dto;

import lombok.Builder;
import lombok.Data;
import org.example.lv_be.common.enums.MemberTier;
import org.example.lv_be.common.enums.Role;

import java.math.BigDecimal;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String phone;
    private String fullName;
    private Role role;
    private Integer points;
    private MemberTier memberTier;
    private BigDecimal commissionRate;
}