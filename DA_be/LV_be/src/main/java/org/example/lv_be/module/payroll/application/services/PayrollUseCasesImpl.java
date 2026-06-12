package org.example.lv_be.module.payroll.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.payroll.application.dto.response.PayrollResponse;
import org.example.lv_be.module.payroll.application.interfaces.in.ICalculatePayrollUseCase;
import org.example.lv_be.module.payroll.application.interfaces.in.IMarkPayrollAsPaidUseCase;
import org.example.lv_be.module.payroll.application.interfaces.out.IStaffClient;
import org.example.lv_be.module.payroll.application.mappers.PayrollMapper;
import org.example.lv_be.module.payroll.domain.entity.Payroll;
import org.example.lv_be.module.payroll.domain.enums.AttendanceStatus;
import org.example.lv_be.module.payroll.domain.exception.PayrollDomainException;
import org.example.lv_be.module.payroll.domain.repository.IAttendanceRepository;
import org.example.lv_be.module.payroll.domain.repository.ICommissionLogRepository;
import org.example.lv_be.module.payroll.domain.repository.IPayrollRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PayrollUseCasesImpl implements ICalculatePayrollUseCase, IMarkPayrollAsPaidUseCase {

    private final IPayrollRepository payrollRepository;
    private final IAttendanceRepository attendanceRepository;
    private final ICommissionLogRepository commissionLogRepository;
    private final IStaffClient staffClient;
    private final PayrollMapper payrollMapper;

    // Cấu hình tiền phạt đi muộn (Có thể đẩy ra file application.yml nếu muốn)
    private static final BigDecimal PENALTY_PER_LATE_DAY = new BigDecimal("50000");

    @Override
    @Transactional
    public PayrollResponse execute(Long staffId, int month, int year) {
        // 1. Chốt mốc thời gian của tháng cần tính
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // 2. Thu thập dữ liệu từ các nơi
        BigDecimal baseSalary = staffClient.getStaffBaseSalary(staffId);

        BigDecimal totalCommission = commissionLogRepository.sumCommissionByStaffIdAndDateRange(
                staffId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));

        int lateDays = attendanceRepository.countByStaffIdAndStatusAndWorkDateBetween(
                staffId, AttendanceStatus.LATE, startDate, endDate);

        // 3. Tính tổng tiền phạt
        BigDecimal totalDeductions = PENALTY_PER_LATE_DAY.multiply(new BigDecimal(lateDays));

        // 4. Tìm bảng lương cũ (nếu có) để cập nhật, hoặc tạo mới nếu chưa tính bao giờ
        Payroll payroll = payrollRepository.findByStaffIdAndMonthAndYear(staffId, month, year)
                .orElse(Payroll.builder()
                        .staffId(staffId)
                        .month(month)
                        .year(year)
                        .build());

        // 5. Gắn số liệu và kích hoạt công thức tính Thực Lĩnh (Net Pay) ở tầng Domain
        payroll.setBaseSalary(baseSalary);
        payroll.setTotalCommission(totalCommission != null ? totalCommission : BigDecimal.ZERO);
        payroll.calculateNetPay(totalDeductions);

        // 6. Lưu xuống DB
        Payroll savedPayroll = payrollRepository.sourceSave(payroll);

        // 7. Lấy tên nhân viên và Map sang DTO để trả về Frontend
        Map<Long, String> activeStaffs = staffClient.getAllActiveStaffs();
        String staffName = activeStaffs.getOrDefault(staffId, "Không xác định");

        return payrollMapper.toPayrollResponse(savedPayroll, staffName);
    }

    // ================== LOGIC CHỐT LƯƠNG (XÁC NHẬN THANH TOÁN) ==================
    @Override
    @Transactional
    public void execute(Long payrollId) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new PayrollDomainException("Không tìm thấy bảng lương."));

        payroll.markAsPaid(); // Xác nhận khóa sổ
        payrollRepository.sourceSave(payroll);
    }
}