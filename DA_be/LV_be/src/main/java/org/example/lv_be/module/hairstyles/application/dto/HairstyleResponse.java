package org.example.lv_be.module.hairstyles.application.dto;

import lombok.*;
import org.example.lv_be.common.enums.FaceShape;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class HairstyleResponse {
    private Long id;
    private String name;
    private FaceShape faceShape;
    private String imageUrl;
    private String description;
    private LocalDateTime createdAt;
    private boolean active;
}