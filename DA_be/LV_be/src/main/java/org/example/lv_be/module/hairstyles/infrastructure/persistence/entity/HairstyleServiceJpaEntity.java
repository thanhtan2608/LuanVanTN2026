package org.example.lv_be.module.hairstyles.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hairstyle_services")
@IdClass(HairstyleServiceId.class) // Chỉ định cấu hình lớp khóa chính liên hợp bổ trợ
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HairstyleServiceJpaEntity {

    @Id
    @Column(name = "hairstyle_id")
    private Long hairstyleId;

    @Id
    @Column(name = "service_id")
    private Long serviceId;
}