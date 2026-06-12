package org.example.lv_be.module.payroll.application.interfaces.out;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Map;

public interface IStaffClient {
    BigDecimal getStaffBaseSalary(Long staffId);
    LocalTime getShiftStartTime(Long staffId);

    // 🌟 MỚI: Lấy danh sách ID và Tên của tất cả nhân viên (Role: STAFF) đang Active
    Map<Long, String> getAllActiveStaffs();
}