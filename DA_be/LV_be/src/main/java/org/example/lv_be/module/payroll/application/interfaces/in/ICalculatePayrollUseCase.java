package org.example.lv_be.module.payroll.application.interfaces.in;

import org.example.lv_be.module.payroll.application.dto.response.PayrollResponse;

public interface ICalculatePayrollUseCase {
    PayrollResponse execute(Long staffId, int month, int year);
}