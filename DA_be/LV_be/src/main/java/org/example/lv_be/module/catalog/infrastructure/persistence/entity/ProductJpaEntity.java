package org.example.lv_be.module.catalog.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "stock_quantity")
    private int stockQuantity = 0;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Column(name = "is_active")
    private boolean active = true;
}