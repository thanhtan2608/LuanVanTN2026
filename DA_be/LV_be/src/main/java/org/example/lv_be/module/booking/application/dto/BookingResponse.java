package org.example.lv_be.module.booking.application.dto;

import lombok.*;
import org.example.lv_be.common.enums.BookingStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private String code;
    private Long branchId;
    private Long customerId;
    private Long staffId;
    private Long hairstyleId;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;
    private String notes;
    private Integer version;
    private LocalDateTime createdAt;
}