package org.example.lv_be.module.booking.application.dto.shift;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftResponse {
    private Long id;
    private Long staffId;
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean active;
}