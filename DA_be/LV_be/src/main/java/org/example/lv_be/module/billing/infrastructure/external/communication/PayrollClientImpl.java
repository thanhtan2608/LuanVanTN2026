package org.example.lv_be.module.billing.infrastructure.external.communication;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.billing.application.interfaces.out.IPayrollClient;
import org.example.lv_be.module.payroll.application.interfaces.in.IAddCommissionUseCase;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PayrollClientImpl implements IPayrollClient {

    private final IAddCommissionUseCase addCommissionUseCase;

    @Override
    public void sendCommissionData(Long staffId, Long invoiceId, BigDecimal amount) {
        // Gọi thẳng hàm execute của Payroll để ghi nhận hoa hồng
        addCommissionUseCase.execute(staffId, invoiceId, amount);
    }
}