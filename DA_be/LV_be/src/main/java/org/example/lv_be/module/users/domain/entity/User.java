package org.example.lv_be.module.users.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.lv_be.common.enums.MemberTier;
import org.example.lv_be.common.enums.Role;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {
    private Long id;
    private String phone;
    private String password;
    private String fullName;
    private Role role;
    private Long branchId;
    private Integer points;
    private MemberTier memberTier;
    private BigDecimal commissionRate;
    private boolean isDeleted;
    private boolean isActive;
    private LocalDateTime createdAt;

    // Default constructor đóng vai trò khởi tạo các giá trị mặc định nghiệp vụ
    // Thay thế hoàn toàn cho @PrePersist của JPA
    public User() {
        this.createdAt = LocalDateTime.now();
        this.points = 0;
        this.memberTier = MemberTier.NEW;
        this.commissionRate = BigDecimal.ZERO;
        this.isDeleted = false;
        this.isActive = true;
    }

    // Các hàm chứa logic nghiệp vụ cốt lõi (Domain Logic)
    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }

    public void lockAccount() {
        this.isActive = false;
    }
}