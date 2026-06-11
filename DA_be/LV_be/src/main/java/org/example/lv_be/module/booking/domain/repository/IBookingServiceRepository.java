package org.example.lv_be.module.booking.domain.repository;

import org.example.lv_be.module.booking.domain.entity.BookingService;
import java.util.List;

public interface IBookingServiceRepository {
    // Lấy ra tất cả các dịch vụ (Cắt, Uốn, Nhuộm...) nằm trong 1 đơn đặt lịch hẹn tổng thể
    List<BookingService> findByBookingId(Long bookingId);

    // Lưu hàng loạt danh sách các dịch vụ khách chọn vào bảng booking_services
    void sourceSaveAll(List<BookingService> bookingServices);
    void deleteLink(Long bookingId, Long serviceId);
}