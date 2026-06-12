package org.example.lv_be.module.payroll.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckInRequest {
    @NotNull(message = "ID nhân viên không được để trống")
    private Long staffId;
    // Có thể thêm tọa độ GPS (lat, lng) vào đây nếu làm chức năng check vị trí
}