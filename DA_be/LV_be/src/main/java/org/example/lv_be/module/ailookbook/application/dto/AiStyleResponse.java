package org.example.lv_be.module.ailookbook.application.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AiStyleResponse {
    private Long id;
    private Long userId;
    private Long lookbookItemId;
    private String sourceImageUrl;
    private String resultImageUrl;
    private String status;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}