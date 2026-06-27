package org.example.lv_be.module.users.domain.entity;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
public class Employee {
    private Long userId; // Liên kết 1-1 với ID của User
    private Long branchId; // NULL nếu là Admin hệ thống
    private BigDecimal baseSalary;
    private BigDecimal commissionRate; // Lưu dạng phần trăm (VD: 10.5 = 10.5%)

    // Thuộc tính tùy chọn
    private User user;
    // ==========================================
    // LOGIC NGHIỆP VỤ
    // ==========================================

    /**
     * Tính toán số tiền hoa hồng thợ nhận được từ một hóa đơn/dịch vụ
     */
    public BigDecimal calculateCommissionAmount(BigDecimal servicePrice) {
        if (this.commissionRate == null || this.commissionRate.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        // Công thức: Giá dịch vụ * (Tỉ lệ hoa hồng / 100)
        return servicePrice.multiply(this.commissionRate)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    /**
     * Kiểm tra xem nhân sự này có đang làm việc tại chi nhánh cụ thể không
     */
    public boolean isWorkingAt(Long targetBranchId) {
        // Admin quản lý toàn hệ thống (branchId = null) nên coi như thuộc mọi chi nhánh
        if (this.user != null && this.user.isSuperAdmin()) {
            return true;
        }
        return this.branchId != null && this.branchId.equals(targetBranchId);
    }
}