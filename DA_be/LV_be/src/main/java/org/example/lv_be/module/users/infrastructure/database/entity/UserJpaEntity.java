package org.example.lv_be.module.users.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.lv_be.common.enums.MemberTier;
import org.example.lv_be.common.enums.Role;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
public class UserJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 20)
    private String phone;
    @Column(length = 255)
    private String password;
    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Role role;
    @Column(name = "branch_id")
    private Long branchId;
    @Column(columnDefinition = "int default 0")
    private Integer points;
    @Enumerated(EnumType.STRING)
    private MemberTier memberTier;
    @Column(name = "commission_rate", precision = 5, scale = 2)
    private BigDecimal commissionRate;
    private boolean isDeleted;
    private boolean isActive;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}