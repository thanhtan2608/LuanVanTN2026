package org.example.lv_be.module.booking.application.dto;

import lombok.*;
import org.example.lv_be.common.enums.BookingStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailResponse {
    private Long bookingId;
    private String code;
    private Long branchId;
    private String branchName;
    private String staffName;
    private String hairstyleName; // Tên kiểu mẫu từ AI Lookbook nếu có
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;
    private String notes;
    private double totalPrice;
    private List<SelectedServiceDto> services;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectedServiceDto {
        private Long serviceId;
        private String serviceName;
        private double priceAtBooking;
    }
}