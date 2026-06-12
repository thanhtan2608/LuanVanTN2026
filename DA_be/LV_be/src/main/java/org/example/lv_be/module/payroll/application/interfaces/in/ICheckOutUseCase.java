package org.example.lv_be.module.payroll.application.interfaces.in;

import org.example.lv_be.module.payroll.application.dto.response.AttendanceResponse;

public interface ICheckOutUseCase {
    AttendanceResponse execute(Long staffId);
}