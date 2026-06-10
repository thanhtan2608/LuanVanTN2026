package org.example.lv_be.module.hairstyles.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class SyncHairstyleServicesRequest {
    @NotNull(message = "Danh sách ID dịch vụ liên kết không được null")
    private List<Long> serviceIds; // Mảng các ID dịch vụ muốn gán cho kiểu tóc này
}