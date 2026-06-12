package org.example.lv_be.module.payroll.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.lv_be.module.payroll.domain.enums.AttendanceStatus;

@Getter
@Setter
public class OverrideAttendanceRequest {
    @NotNull
    private AttendanceStatus newStatus; // Ví dụ: Quản lý chọn EXCUSED (Có phép)
}
