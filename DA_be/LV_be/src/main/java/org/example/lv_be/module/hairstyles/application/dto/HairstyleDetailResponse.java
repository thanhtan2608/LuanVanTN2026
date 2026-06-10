package org.example.lv_be.module.hairstyles.application.dto;

import lombok.*;
import org.example.lv_be.common.enums.FaceShape;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class HairstyleDetailResponse {
    private Long id;
    private String name;
    private FaceShape faceShape;
    private String imageUrl;
    private String description;
    private LocalDateTime createdAt;
    private boolean active;

    // 🌟 DANH SÁCH DỊCH VỤ THẬT LIÊN KẾT (Bốc thông tin từ module Catalog sang)
    private List<AssociatedServiceInfo> actualServices;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssociatedServiceInfo {
        private Long serviceId;
        private String serviceName;
        private double price;
        private int durationMinutes;
    }
}