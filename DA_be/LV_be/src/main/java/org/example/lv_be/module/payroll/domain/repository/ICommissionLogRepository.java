package org.example.lv_be.module.payroll.domain.repository;

import org.example.lv_be.module.payroll.domain.entity.CommissionLog;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ICommissionLogRepository {
    CommissionLog sourceSave(CommissionLog log);

    // Phục vụ cho việc cộng tổng hoa hồng cuối tháng
    BigDecimal sumCommissionByStaffIdAndDateRange(Long staffId, LocalDateTime startDate, LocalDateTime endDate);
}