package org.example.lv_be.module.ailookbook.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.lv_be.module.ailookbook.domain.enums.AiProcessStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_ai_styles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAiStyleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "lookbook_item_id", nullable = false)
    private Long lookbookItemId;

    @Column(name = "source_image_url", nullable = false, length = 512)
    private String sourceImageUrl;

    @Column(name = "result_image_url", length = 512)
    private String resultImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AiProcessStatus status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}