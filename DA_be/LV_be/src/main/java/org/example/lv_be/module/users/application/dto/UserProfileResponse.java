package org.example.lv_be.module.users.application.dto;

import lombok.Builder;
import lombok.Data;
import org.example.lv_be.common.enums.Role;
import java.math.BigDecimal;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String phone;
    private String fullName;
    private Role role;
    private boolean isActive;

    // --- Dành riêng cho Khách hàng (Sẽ null nếu là Staff) ---
    private Integer points;
    private String memberTier;

    // --- Dành riêng cho Nhân sự (Sẽ null nếu là Customer) ---
    private Long branchId;
    private BigDecimal baseSalary;
    private BigDecimal commissionRate;
}