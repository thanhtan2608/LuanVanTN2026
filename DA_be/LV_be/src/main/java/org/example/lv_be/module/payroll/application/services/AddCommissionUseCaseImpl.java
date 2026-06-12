package org.example.lv_be.module.payroll.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.payroll.application.interfaces.in.IAddCommissionUseCase;
import org.example.lv_be.module.payroll.domain.entity.CommissionLog;
import org.example.lv_be.module.payroll.domain.repository.ICommissionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AddCommissionUseCaseImpl implements IAddCommissionUseCase {

    private final ICommissionLogRepository commissionLogRepository;

    @Override
    @Transactional
    public void execute(Long staffId, Long invoiceId, BigDecimal amount) {
        CommissionLog log = CommissionLog.builder()
                .staffId(staffId)
                .invoiceId(invoiceId)
                .amount(amount)
                .createdAt(LocalDateTime.now()) // Thời điểm khách thanh toán xong
                .build();

        commissionLogRepository.sourceSave(log);
    }
}