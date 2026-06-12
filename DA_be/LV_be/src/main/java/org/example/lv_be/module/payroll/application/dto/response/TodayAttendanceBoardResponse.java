package org.example.lv_be.module.payroll.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.lv_be.module.payroll.domain.enums.AttendanceStatus;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayAttendanceBoardResponse {

    // 👤 Thông tin nhân viên (Gom sẵn tên để Frontend không phải gọi API phụ)
    private Long staffId;
    private String staffName;

    // ⏰ Thời gian ca làm quy định
    private LocalTime shiftStartTime;

    // 📅 Thời gian điểm danh thực tế
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    // 🚦 Trạng thái (ON_TIME, LATE, ABSENT, EXCUSED)
    private AttendanceStatus status;
}