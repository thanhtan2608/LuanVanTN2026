package org.example.lv_be.module.hairstyles.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.lv_be.common.enums.FaceShape;
import java.time.LocalDateTime;

@Entity
@Table(name = "hairstyles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HairstyleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "face_shape", nullable = false, columnDefinition = "ENUM('OVAL', 'ROUND', 'SQUARE', 'LONG', 'HEART')")
    private FaceShape faceShape;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Column(name = "is_active")
    private boolean active = true;
}