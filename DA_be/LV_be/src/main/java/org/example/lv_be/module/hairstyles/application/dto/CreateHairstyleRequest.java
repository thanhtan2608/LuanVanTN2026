package org.example.lv_be.module.hairstyles.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.lv_be.common.enums.FaceShape;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateHairstyleRequest {
    @NotBlank(message = "Tên kiểu tóc không được phép để trống")
    private String name;

    @NotNull(message = "Dáng khuôn mặt phù hợp không được để trống")
    private FaceShape faceShape; // Enum OVAL, ROUND, SQUARE, LONG, HEART từ common

    private String description;
}