package org.example.lv_be.module.catalog.application.dto.service;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ServiceResponse {
    private Long id;
    private Long categoryId;
    private String name;
    private double price;
    private int durationMinutes;
    private String imageUrl;
    private boolean active;
}