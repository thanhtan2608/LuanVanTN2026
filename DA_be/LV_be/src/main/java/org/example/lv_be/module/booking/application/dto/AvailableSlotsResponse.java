package org.example.lv_be.module.booking.application.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableSlotsResponse {
    private Long staffId;
    private List<String> availableSlots; // Định dạng danh sách chuỗi ["08:00", "08:30", "09:00",...]
}