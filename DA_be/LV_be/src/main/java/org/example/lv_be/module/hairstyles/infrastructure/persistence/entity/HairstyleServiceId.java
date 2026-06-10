package org.example.lv_be.module.hairstyles.infrastructure.persistence.entity;

import lombok.*;
import java.io.Serializable;

/**
 * Lớp định danh đại diện cho Khóa chính liên hợp (Composite Primary Key)
 * của bảng trung gian Nhiều - Nhiều 'hairstyle_services' theo chuẩn JPA.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HairstyleServiceId implements Serializable {
    private Long hairstyleId;
    private Long serviceId;
}