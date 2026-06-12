package org.example.lv_be.module.users.application.interfaces;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Map;

public interface IStaffQueryUseCase {
    BigDecimal getBaseSalary(Long staffId);
    LocalTime getShiftStartTime(Long staffId);

    // Lấy danh sách thợ đang làm việc (ID và Tên)
    Map<Long, String> getAllActiveStaffs();
}
