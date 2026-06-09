package org.example.lv_be.module.ailookbook.application.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LookbookResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String gender;
    private String prompt;
    private Long hairstyleId;
    private boolean isActive;
    private LocalDateTime createdAt;
}