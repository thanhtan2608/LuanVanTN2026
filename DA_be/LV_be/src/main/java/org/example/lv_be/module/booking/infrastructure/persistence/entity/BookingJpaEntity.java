package org.example.lv_be.module.booking.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.lv_be.common.enums.BookingStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @Column(name = "hairstyle_id")
    private Long hairstyleId;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('PENDING', 'CONFIRMED', 'CHECKED_IN', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'NO_SHOW')")
    private BookingStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Version // 🌟 Kích hoạt cơ chế chống trùng slot lịch đặt ca (Optimistic Locking) từ cột version int(11)
    @Column(name = "version")
    private Integer version;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}