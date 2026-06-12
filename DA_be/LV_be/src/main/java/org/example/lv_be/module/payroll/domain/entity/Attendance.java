package org.example.lv_be.module.payroll.domain.entity;

import lombok.*;
import org.example.lv_be.module.payroll.domain.enums.AttendanceStatus;
import org.example.lv_be.module.payroll.domain.exception.PayrollDomainException;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {
    private Long id;
    private Long staffId;
    private LocalDate workDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    @Builder.Default
    private AttendanceStatus status = AttendanceStatus.ABSENT; // Mặc định là vắng mặt cho đến khi quét vân tay/check-in

    // 🧠 LOGIC NGHIỆP VỤ 1: Hệ thống TỰ ĐỘNG phán xét khi nhân viên vừa check-in
    public void calculateStatusOnCheckIn(LocalTime shiftStartTime) {
        if (this.checkInTime == null) {
            this.status = AttendanceStatus.ABSENT;
            return;
        }

        // Trễ 15 phút so với giờ bắt đầu ca
        if (this.checkInTime.isAfter(shiftStartTime.plusMinutes(15))) {
            this.status = AttendanceStatus.LATE;
        } else {
            this.status = AttendanceStatus.ON_TIME;
        }
    }

    // 🧠 LOGIC NGHIỆP VỤ 2: Quản lý can thiệp bằng tay (Manual Override)
    public void overrideStatusByManager(AttendanceStatus newStatus) {
        if (newStatus == null) {
            throw new PayrollDomainException("Trạng thái cập nhật không được để trống.");
        }
        this.status = newStatus;
    }
}