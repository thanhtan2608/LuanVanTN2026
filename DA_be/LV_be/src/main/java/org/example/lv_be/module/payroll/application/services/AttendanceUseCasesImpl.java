package org.example.lv_be.module.payroll.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.payroll.application.dto.request.CheckInRequest;
import org.example.lv_be.module.payroll.application.dto.request.OverrideAttendanceRequest;
import org.example.lv_be.module.payroll.application.dto.response.AttendanceResponse;
import org.example.lv_be.module.payroll.application.dto.response.TodayAttendanceBoardResponse;
import org.example.lv_be.module.payroll.application.interfaces.in.ICheckInUseCase;
import org.example.lv_be.module.payroll.application.interfaces.in.ICheckOutUseCase;
import org.example.lv_be.module.payroll.application.interfaces.in.IGetTodayBoardUseCase;
import org.example.lv_be.module.payroll.application.interfaces.in.IOverrideAttendanceUseCase;
import org.example.lv_be.module.payroll.application.interfaces.out.IStaffClient;
import org.example.lv_be.module.payroll.application.mappers.PayrollMapper;
import org.example.lv_be.module.payroll.domain.entity.Attendance;
import org.example.lv_be.module.payroll.domain.enums.AttendanceStatus;
import org.example.lv_be.module.payroll.domain.exception.PayrollDomainException;
import org.example.lv_be.module.payroll.domain.repository.IAttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceUseCasesImpl implements ICheckInUseCase, ICheckOutUseCase, IOverrideAttendanceUseCase, IGetTodayBoardUseCase {

    private final IAttendanceRepository attendanceRepository;
    private final IStaffClient staffClient;
    private final PayrollMapper payrollMapper; // Dùng Mapper để code sạch hơn

    // ================== LOGIC CHECK-IN ==================
    @Override
    @Transactional
    public AttendanceResponse execute(CheckInRequest request) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        attendanceRepository.findByStaffIdAndWorkDate(request.getStaffId(), today)
                .ifPresent(a -> { throw new PayrollDomainException("Nhân viên này đã được Check-in rồi!"); });

        LocalTime shiftStartTime = staffClient.getShiftStartTime(request.getStaffId());

        Attendance attendance = Attendance.builder()
                .staffId(request.getStaffId())
                .workDate(today)
                .checkInTime(now)
                .build();

        // Kích hoạt logic tự phán xét trạng thái (Đúng giờ / Đi muộn)
        attendance.calculateStatusOnCheckIn(shiftStartTime);

        return payrollMapper.toAttendanceResponse(attendanceRepository.sourceSave(attendance));
    }

    // ================== LOGIC CHECK-OUT ==================
    @Override
    @Transactional
    public AttendanceResponse execute(Long staffId) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByStaffIdAndWorkDate(staffId, today)
                .orElseThrow(() -> new PayrollDomainException("Nhân viên chưa Check-in nên không thể Check-out!"));

        attendance.setCheckOutTime(LocalTime.now());
        return payrollMapper.toAttendanceResponse(attendanceRepository.sourceSave(attendance));
    }

    // ================== LOGIC SỬA TRẠNG THÁI (QUẢN LÝ) ==================
    @Override
    @Transactional
    public AttendanceResponse execute(Long attendanceId, OverrideAttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new PayrollDomainException("Không tìm thấy bản ghi chấm công có ID: " + attendanceId));

        attendance.overrideStatusByManager(request.getNewStatus());
        return payrollMapper.toAttendanceResponse(attendanceRepository.sourceSave(attendance));
    }

    // ================== LOGIC LẤY BẢNG THEO DÕI HÔM NAY ==================
    @Override
    public List<TodayAttendanceBoardResponse> execute() {
        LocalDate today = LocalDate.now();
        List<TodayAttendanceBoardResponse> board = new ArrayList<>();

        Map<Long, String> activeStaffs = staffClient.getAllActiveStaffs();

        for (Map.Entry<Long, String> entry : activeStaffs.entrySet()) {
            Long staffId = entry.getKey();
            String staffName = entry.getValue();
            LocalTime shiftStartTime = staffClient.getShiftStartTime(staffId);

            Optional<Attendance> attendanceOpt = attendanceRepository.findByStaffIdAndWorkDate(staffId, today);

            TodayAttendanceBoardResponse response = TodayAttendanceBoardResponse.builder()
                    .staffId(staffId)
                    .staffName(staffName)
                    .shiftStartTime(shiftStartTime)
                    .build();

            if (attendanceOpt.isPresent()) {
                Attendance att = attendanceOpt.get();
                response.setCheckInTime(att.getCheckInTime());
                response.setCheckOutTime(att.getCheckOutTime());
                response.setStatus(att.getStatus());
            } else {
                response.setStatus(AttendanceStatus.ABSENT);
            }
            board.add(response);
        }
        return board;
    }
}