package org.example.lv_be.module.payroll.application.interfaces.in;

import org.example.lv_be.module.payroll.application.dto.request.CheckInRequest;
import org.example.lv_be.module.payroll.application.dto.response.AttendanceResponse;

public interface ICheckInUseCase {
    AttendanceResponse execute(CheckInRequest request);
}