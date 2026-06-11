package org.example.lv_be.module.booking.application.dto.shift;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShiftRequest {
    @NotNull(message = "Mã nhân viên không được để trống")
    private Long staffId;

    @NotNull(message = "Ngày xếp ca không được để trống")
    private LocalDate shiftDate;

    @NotNull(message = "Giờ bắt đầu vào ca không được để trống")
    private LocalTime startTime;

    @NotNull(message = "Giờ kết thúc ca không được để trống")
    private LocalTime endTime;
}