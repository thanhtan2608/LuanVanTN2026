package org.example.lv_be.module.booking.domain.entity;

import lombok.*;
import org.example.lv_be.module.booking.domain.exception.BookingDomainException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingService {
    private Long id;                // bigint(20) AUTO_INCREMENT
    private Long bookingId;         // bigint(20) -> cột booking_id
    private Long serviceId;         // bigint(20) -> cột service_id (Mã dịch vụ từ Catalog)
    private double priceAtBooking;  // decimal(10,2) -> cột price_at_booking

    public void validateSelf() {
        if (this.bookingId == null) {
            throw new BookingDomainException("Chi tiết dịch vụ phải gắn liền với một mã đơn đặt lịch hẹn!");
        }
        if (this.serviceId == null) {
            throw new BookingDomainException("Mã dịch vụ kỹ thuật tóc chọn làm không được trống!");
        }
        if (this.priceAtBooking < 0) {
            throw new BookingDomainException("Giá tiền dịch vụ chốt giữ chỗ không được nhỏ hơn 0!");
        }
    }
}