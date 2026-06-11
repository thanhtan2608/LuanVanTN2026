package org.example.lv_be.module.booking.application.dto.modification;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddExtraServiceRequest {
    @NotNull(message = "Mã dịch vụ phát sinh bắt buộc phải có")
    private Long serviceId;
}