package org.example.lv_be.module.payroll.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.lv_be.module.payroll.domain.enums.AttendanceStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponse {

    private Long id;

    // 👤 Thông tin nhân viên
    private Long staffId;

    // 📅 Thời gian điểm danh
    private LocalDate workDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    // 🚦 Trạng thái (ON_TIME, LATE, ABSENT, EXCUSED)
    private AttendanceStatus status;
}