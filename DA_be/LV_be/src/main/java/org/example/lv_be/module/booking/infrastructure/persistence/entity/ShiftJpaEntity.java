package org.example.lv_be.module.booking.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "shifts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "staff_id", nullable = false)
    private Long staffId; // ID của thợ làm tóc (User mang role STYLIST)

    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate; // Ngày trực ca

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime; // Giờ bắt đầu ca trực

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime; // Giờ kết thúc ca trực

    @Column(name = "is_active", nullable = false)
    private boolean active = true; // Trạng thái ca làm việc (true = hoạt động, false = hủy ca)
}