package org.example.lv_be.module.billing.application.interfaces.out;

import java.math.BigDecimal;

public interface IPayrollClient {
    /**
     * Bắn tín hiệu sang hệ thống Lương thưởng để cộng hoa hồng cho thợ
     */
    void sendCommissionData(Long staffId, Long invoiceId, BigDecimal amount);
}