package org.example.lv_be.module.ailookbook.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.lv_be.module.ailookbook.domain.enums.GenderCategory;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "lookbook_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE lookbook_items SET is_deleted = true, is_active = false WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class LookbookJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", nullable = false, length = 512)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GenderCategory gender;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String prompt;

    @Column(name = "hairstyle_id")
    private Long hairstyleId;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}