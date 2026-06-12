package org.example.lv_be.module.payroll.infrastructure.external.communication;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.payroll.application.interfaces.out.IStaffClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StaffClientImpl implements IStaffClient {

    // Nếu đã làm module Users, bạn có thể Inject UseCase của module Users vào đây:
    // private final IGetStaffDetailsUseCase getStaffDetailsUseCase;

    @Override
    public BigDecimal getStaffBaseSalary(Long staffId) {
        // Thực tế: Lấy từ bảng users/staff_profiles
        // Mock data tạm thời để hệ thống chạy được
        return new BigDecimal("5000000"); // Lương cứng 5 triệu
    }

    @Override
    public LocalTime getShiftStartTime(Long staffId) {
        // Thực tế: Lấy từ module Shifts/Booking
        return LocalTime.of(8, 0); // Ca làm việc bắt đầu lúc 8:00 sáng
    }

    @Override
    public Map<Long, String> getAllActiveStaffs() {
        // Thực tế: Gọi hàm repository lấy danh sách từ bảng Users có role = STAFF
        Map<Long, String> mockStaffs = new HashMap<>();
        mockStaffs.put(1L, "Nguyễn Văn Thợ Chính");
        mockStaffs.put(2L, "Trần Thị Gội Đầu");
        return mockStaffs;
    }
}