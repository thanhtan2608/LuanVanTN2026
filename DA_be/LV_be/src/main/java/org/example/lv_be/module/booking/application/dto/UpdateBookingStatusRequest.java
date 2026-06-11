package org.example.lv_be.module.booking.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.lv_be.common.enums.BookingStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingStatusRequest {
    @NotNull(message = "Trạng thái cập nhật không được trống")
    private BookingStatus status;
}