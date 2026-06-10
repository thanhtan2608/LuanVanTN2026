package org.example.lv_be.module.catalog.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.lv_be.common.enums.ItemType;
import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "ENUM('SERVICE', 'PRODUCT')")
    private ItemType type;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Column(name = "is_active")
    private boolean active = true;
}