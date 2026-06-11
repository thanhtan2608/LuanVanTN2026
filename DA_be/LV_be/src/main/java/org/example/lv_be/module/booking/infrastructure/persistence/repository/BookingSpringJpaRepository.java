package org.example.lv_be.module.booking.infrastructure.persistence.repository;

import org.example.lv_be.module.booking.infrastructure.persistence.entity.BookingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingSpringJpaRepository extends JpaRepository<BookingJpaEntity, Long> {

    Optional<BookingJpaEntity> findByCode(String code);

    List<BookingJpaEntity> findByCustomerIdOrderByBookingDateDesc(Long customerId);

    List<BookingJpaEntity> findByStaffIdAndBookingDate(Long staffId, LocalDate bookingDate);

    List<BookingJpaEntity> findByBranchIdAndBookingDate(Long branchId, LocalDate bookingDate);
}