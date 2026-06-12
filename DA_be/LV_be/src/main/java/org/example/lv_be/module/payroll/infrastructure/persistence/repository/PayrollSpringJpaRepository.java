package org.example.lv_be.module.payroll.infrastructure.persistence.repository;

import org.example.lv_be.module.payroll.infrastructure.persistence.entity.PayrollJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayrollSpringJpaRepository extends JpaRepository<PayrollJpaEntity, Long> {
    Optional<PayrollJpaEntity> findByStaffIdAndMonthAndYear(Long staffId, int month, int year);
}