package org.example.lv_be.module.users.application.interfaces.in;

import org.example.lv_be.module.users.application.dto.UserProfileResponse;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface IStaffQueryUseCase {
    BigDecimal getBaseSalary(Long staffId);
    LocalTime getShiftStartTime(Long staffId);

    // Lấy danh sách thợ đang làm việc (ID và Tên)
    Map<Long, String> getAllActiveStaffs();
    List<UserProfileResponse> execute(Long branchId);

}
