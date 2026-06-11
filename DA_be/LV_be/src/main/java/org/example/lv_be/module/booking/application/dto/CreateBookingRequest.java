package org.example.lv_be.module.booking.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {
    @NotNull(message = "Chi nhánh không được phép để trống")
    private Long branchId;

    private Long customerId; // Có thể null đối với khách vãng lai đặt tại quầy

    @NotNull(message = "Bắt buộc phải chỉ định nhân viên phục vụ")
    private Long staffId;

    private Long hairstyleId; // Có thể null nếu khách không lựa chọn qua AI Lookbook

    @NotNull(message = "Ngày đặt lịch không được để trống")
    private LocalDate bookingDate;

    @NotNull(message = "Giờ bắt đầu cuộc hẹn không được để trống")
    private LocalTime startTime;

    @NotEmpty(message = "Bắt buộc phải chọn ít nhất một dịch vụ làm tóc")
    private List<Long> serviceIds;

    private String notes;
}