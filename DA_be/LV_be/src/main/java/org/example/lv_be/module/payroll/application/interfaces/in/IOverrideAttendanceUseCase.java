package org.example.lv_be.module.payroll.application.interfaces.in;

import org.example.lv_be.module.payroll.application.dto.request.OverrideAttendanceRequest;
import org.example.lv_be.module.payroll.application.dto.response.AttendanceResponse;

public interface IOverrideAttendanceUseCase {
    AttendanceResponse execute(Long attendanceId, OverrideAttendanceRequest request);
}