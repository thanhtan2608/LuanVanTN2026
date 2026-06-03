package org.example.lv_be.common.enums;

public enum BookingStatus {
    PENDING,      // Chờ xác nhận
    CONFIRMED,    // Đã xác nhận
    CHECKED_IN,   // Khách đã đến
    IN_PROGRESS,  // Đang phục vụ
    COMPLETED,    // Đã hoàn thành (Đã thanh toán)
    CANCELLED     // Đã hủy
}
