package org.example.lv_be.module.payroll.domain.entity;

import lombok.*;
import org.example.lv_be.module.payroll.domain.enums.PayrollStatus;
import org.example.lv_be.module.payroll.domain.exception.PayrollDomainException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {
    private Long id;
    private Long staffId;
    private int month;
    private int year;

    private BigDecimal baseSalary;
    private BigDecimal totalCommission;
    private BigDecimal netPay; // Khớp cột net_pay trong DB

    @Builder.Default
    private PayrollStatus status = PayrollStatus.UNPAID; // Default theo DB

    private LocalDateTime createdAt;

    // 🧠 LOGIC NGHIỆP VỤ 1: Công thức chốt lương thực lĩnh
    public void calculateNetPay(BigDecimal totalDeductions) {
        if (this.baseSalary == null) this.baseSalary = BigDecimal.ZERO;
        if (this.totalCommission == null) this.totalCommission = BigDecimal.ZERO;
        if (totalDeductions == null) totalDeductions = BigDecimal.ZERO;

        // Thực lĩnh = Lương cứng + Hoa hồng - Phạt (Vì DB không lưu cột phạt nên ta trừ thẳng)
        this.netPay = this.baseSalary.add(this.totalCommission).subtract(totalDeductions);

        // Đảm bảo lương không bị âm nếu thợ bị phạt quá nhiều
        if (this.netPay.compareTo(BigDecimal.ZERO) < 0) {
            this.netPay = BigDecimal.ZERO;
        }
    }

    // 🧠 LOGIC NGHIỆP VỤ 2: Chuyển trạng thái khi đã phát lương
    public void markAsPaid() {
        if (this.status == PayrollStatus.PAID) {
            throw new PayrollDomainException("Bảng lương này đã được thanh toán rồi, không thể thanh toán lại!");
        }
        this.status = PayrollStatus.PAID;
    }
}