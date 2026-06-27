package org.example.lv_be.module.users.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.lv_be.common.enums.MemberTier;

@Entity
@Table(name = "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerJpaEntity {
    @Id
    @Column(name = "user_id")
    private Long userId; // Dùng chung ID với UserJpaEntity

    private Integer points;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_tier")
    private MemberTier memberTier;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;
}