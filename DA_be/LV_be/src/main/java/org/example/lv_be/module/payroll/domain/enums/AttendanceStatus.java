package org.example.lv_be.module.payroll.domain.enums;

public enum AttendanceStatus {
    ON_TIME,    // Đi làm đúng giờ
    LATE,       // Đi muộn (Sẽ bị trừ tiền khi chốt lương)
    ABSENT,     // Nghỉ không phép
    EXCUSED     // Có phép (Quản lý châm chước, không bị trừ tiền)
}