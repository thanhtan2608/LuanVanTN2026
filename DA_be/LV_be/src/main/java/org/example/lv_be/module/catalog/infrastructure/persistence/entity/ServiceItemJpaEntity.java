package org.example.lv_be.module.catalog.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Column(name = "is_active")
    private boolean active = true;
}