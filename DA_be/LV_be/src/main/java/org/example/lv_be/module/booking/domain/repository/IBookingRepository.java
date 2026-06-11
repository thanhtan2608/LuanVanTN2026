package org.example.lv_be.module.booking.domain.repository;

import org.example.lv_be.module.booking.domain.entity.Booking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IBookingRepository {
    Optional<Booking> findById(Long id);

    Optional<Booking> findByCode(String code);
    List<Booking> findByCustomerId(Long customerId);

    // 🧠 THUẬT TOÁN CHIA SLOT: Tìm tất cả lịch hẹn của một thợ (staffId) trong một ngày cụ thể
    // để kiểm tra xem khung giờ [start_time, end_time] định đặt có bị đè/kẹt vào ca làm của ai khác không.
    List<Booking> findByStaffIdAndBookingDate(Long staffId, LocalDate bookingDate);

    // Lấy toàn bộ lịch hẹn trong ngày tại chi nhánh để Admin theo dõi Dashboard điều phối bàn làm việc
    List<Booking> findByBranchIdAndBookingDate(Long branchId, LocalDate bookingDate);

    Booking sourceSave(Booking booking);

}