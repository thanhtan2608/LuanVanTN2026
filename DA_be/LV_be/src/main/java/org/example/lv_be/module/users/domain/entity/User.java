package org.example.lv_be.module.users.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.example.lv_be.common.enums.Role;
import java.time.LocalDateTime;

@Data
@Builder
public class User {
    private Long id;
    private String phone;
    private String password; // Đã mã hóa
    private String fullName;
    private Role role;
    private boolean isActive;
    private LocalDateTime createdAt;
    // ==========================================
    // LOGIC NGHIỆP VỤ
    // ==========================================

    /**
     * Khóa tài khoản
     */
    public void lockAccount() {
        this.isActive = false;
    }

    /**
     * Mở khóa tài khoản
     */
    public void unlockAccount() {
        this.isActive = true;
    }

    /**
     * Kiểm tra xem User có quyền truy cập hệ thống không
     */
    public boolean canAccessSystem() {
        return this.isActive;
    }

    /**
     * Kiểm tra xem user có phải là quản trị viên không
     */
    public boolean isSuperAdmin() {
        return this.role == Role.ADMIN;
    }
}