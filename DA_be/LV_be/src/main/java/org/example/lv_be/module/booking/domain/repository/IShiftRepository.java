package org.example.lv_be.module.booking.domain.repository;

import org.example.lv_be.module.booking.domain.entity.Shift;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IShiftRepository {

    Optional<Shift> findById(Long id);

    /**
     * 🧠 THUẬT TOÁN KHOÁ Chìa Vàng: Tìm ca làm việc đăng ký của một thợ cụ thể trong ngày.
     * Giúp hệ thống biết ngày hôm đó thợ làm ca gãy, ca sáng hay ca tối để bẻ slot giờ rảnh.
     */
    Optional<Shift> findByStaffIdAndShiftDate(Long staffId, LocalDate shiftDate);

    // Tìm tất cả các ca làm việc của toàn bộ cửa hàng trong ngày để xếp bàn phục vụ
    List<Shift> findByShiftDate(LocalDate shiftDate);

    // Kiểm tra xem thợ này đã bị xếp trùng ca trong ngày hôm đó chưa
    boolean existsByStaffIdAndShiftDate(Long staffId, LocalDate shiftDate);

    Shift sourceSave(Shift shift);
}