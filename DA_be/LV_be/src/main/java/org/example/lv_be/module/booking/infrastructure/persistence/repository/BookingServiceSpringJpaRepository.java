package org.example.lv_be.module.booking.infrastructure.persistence.repository;

import org.example.lv_be.module.booking.infrastructure.persistence.entity.BookingServiceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingServiceSpringJpaRepository extends JpaRepository<BookingServiceJpaEntity, Long> {
    List<BookingServiceJpaEntity> findByBookingId(Long bookingId);

    @Modifying
    @Query("DELETE FROM BookingServiceJpaEntity bs WHERE bs.bookingId = :bookingId AND bs.serviceId = :serviceId")
    void deleteByBookingIdAndServiceId(@Param("bookingId") Long bookingId, @Param("serviceId") Long serviceId);
}