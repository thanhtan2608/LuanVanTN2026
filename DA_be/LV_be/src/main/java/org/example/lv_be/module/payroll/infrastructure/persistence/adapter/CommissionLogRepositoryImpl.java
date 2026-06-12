package org.example.lv_be.module.payroll.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.payroll.domain.entity.CommissionLog;
import org.example.lv_be.module.payroll.domain.repository.ICommissionLogRepository;
import org.example.lv_be.module.payroll.infrastructure.persistence.entity.CommissionLogJpaEntity;
import org.example.lv_be.module.payroll.infrastructure.persistence.mapper.PayrollPersistenceMapper;
import org.example.lv_be.module.payroll.infrastructure.persistence.repository.CommissionLogSpringJpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class CommissionLogRepositoryImpl implements ICommissionLogRepository {
    private final CommissionLogSpringJpaRepository jpaRepository;
    private final PayrollPersistenceMapper mapper;

    @Override
    public CommissionLog sourceSave(CommissionLog log) {
        CommissionLogJpaEntity jpaEntity = mapper.toJpaEntity(log);
        return mapper.toDomainEntity(jpaRepository.save(jpaEntity));
    }

    @Override
    public BigDecimal sumCommissionByStaffIdAndDateRange(Long staffId, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total = jpaRepository.sumAmountByStaffIdAndDateRange(staffId, startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }
}