package org.example.lv_be.module.booking.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingServiceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Column(name = "price_at_booking", nullable = false)
    private double priceAtBooking; // Đã loại bỏ precision/scale để tránh lỗi tương thích Hibernate 6
}