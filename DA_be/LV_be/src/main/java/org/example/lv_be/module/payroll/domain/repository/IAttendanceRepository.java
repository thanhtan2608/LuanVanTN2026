package org.example.lv_be.module.payroll.domain.repository;

import org.example.lv_be.module.payroll.domain.entity.Attendance;
import org.example.lv_be.module.payroll.domain.enums.AttendanceStatus;

import java.time.LocalDate;
import java.util.Optional;

public interface IAttendanceRepository {
    Optional<Attendance> findByStaffIdAndWorkDate(Long staffId, LocalDate workDate);

    Attendance sourceSave(Attendance attendance);

    // 🚀 Dùng hàm này để đếm số ngày đi muộn (LATE) trong tháng cực nhanh
    int countByStaffIdAndStatusAndWorkDateBetween(
            Long staffId,
            AttendanceStatus status,
            LocalDate startDate,
            LocalDate endDate
    );
    Optional<Attendance> findById(Long id);
}