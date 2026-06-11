package org.example.lv_be.module.booking.infrastructure.persistence.repository;

import org.example.lv_be.module.booking.infrastructure.persistence.entity.ShiftJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShiftSpringJpaRepository extends JpaRepository<ShiftJpaEntity, Long> {

    // Tìm ca làm việc duy nhất của một thợ trong ngày để tính slot giờ trống
    Optional<ShiftJpaEntity> findByStaffIdAndShiftDate(Long staffId, LocalDate shiftDate);

    // Tìm tất cả ca trực của tiệm trong ngày để quản trị viên bao quát hệ thống
    List<ShiftJpaEntity> findByShiftDate(LocalDate shiftDate);

    // Kiểm tra nhanh xem thợ đã được phân ca ngày hôm đó chưa
    boolean existsByStaffIdAndShiftDate(Long staffId, LocalDate shiftDate);
}