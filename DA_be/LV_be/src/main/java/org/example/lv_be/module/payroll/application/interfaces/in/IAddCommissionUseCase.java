package org.example.lv_be.module.payroll.application.interfaces.in;

import java.math.BigDecimal;

public interface IAddCommissionUseCase {

    /**
     * Ghi nhận hoa hồng cho nhân viên.
     * (Hàm này chạy ngầm, không có Controller gọi trực tiếp).
     */
    void execute(Long staffId, Long invoiceId, BigDecimal amount);

}