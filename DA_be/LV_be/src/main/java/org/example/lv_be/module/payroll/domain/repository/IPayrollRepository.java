package org.example.lv_be.module.payroll.domain.repository;

import org.example.lv_be.module.payroll.domain.entity.Payroll;
import java.util.Optional;

public interface IPayrollRepository {
    Optional<Payroll> findById(Long id);
    Optional<Payroll> findByStaffIdAndMonthAndYear(Long staffId, int month, int year);
    Payroll sourceSave(Payroll payroll);
}