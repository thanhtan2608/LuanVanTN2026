package org.example.lv_be.module.payroll.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.payroll.domain.entity.Payroll;
import org.example.lv_be.module.payroll.domain.repository.IPayrollRepository;
import org.example.lv_be.module.payroll.infrastructure.persistence.entity.PayrollJpaEntity;
import org.example.lv_be.module.payroll.infrastructure.persistence.mapper.PayrollPersistenceMapper;
import org.example.lv_be.module.payroll.infrastructure.persistence.repository.PayrollSpringJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PayrollRepositoryImpl implements IPayrollRepository {
    private final PayrollSpringJpaRepository jpaRepository;
    private final PayrollPersistenceMapper mapper;

    @Override
    public Optional<Payroll> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public Optional<Payroll> findByStaffIdAndMonthAndYear(Long staffId, int month, int year) {
        return jpaRepository.findByStaffIdAndMonthAndYear(staffId, month, year).map(mapper::toDomainEntity);
    }

    @Override
    public Payroll sourceSave(Payroll payroll) {
        PayrollJpaEntity jpaEntity = mapper.toJpaEntity(payroll);
        return mapper.toDomainEntity(jpaRepository.save(jpaEntity));
    }
}