package org.example.lv_be.module.payroll.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.lv_be.module.payroll.domain.enums.PayrollStatus;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollResponse {
    private Long id;

    // 👤 Thông tin nhân viên
    private Long staffId;
    private String staffName; // Bổ sung cho Frontend dễ hiển thị Table

    // 📅 Kỳ lương
    private int month;
    private int year;

    // 💰 Chi tiết tiền nong
    private BigDecimal baseSalary;      // Lương cứng
    private BigDecimal totalCommission; // Tổng hoa hồng
    private BigDecimal netPay;          // Thực lĩnh (Cột net_pay trong DB)

    // 🚦 Trạng thái
    private PayrollStatus status;       // UNPAID / PAID
}