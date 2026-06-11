package org.example.lv_be.module.booking.domain.entity;

import lombok.*;
import org.example.lv_be.module.booking.domain.exception.ShiftDomainException;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift {
    private Long id;                // Khóa chính tự tăng của ca làm
    private Long staffId;           // Mã ID của thợ Stylist được xếp ca
    private LocalDate shiftDate;    // Ngày làm việc cụ thể
    private LocalTime startTime;    // Giờ bắt đầu vào ca (Ví dụ: 08:00:00)
    private LocalTime endTime;      // Giờ kết thúc ca làm (Ví dụ: 14:00:00)
    private boolean active;         // Trạng thái ca làm (Kích hoạt hoặc tạm hủy)

    /**
     * 🧠 RICH DOMAIN BUSINESS LOGIC 1: Tự xác thực tính hợp lệ của ca làm
     */
    public void validateSelf() {
        if (this.staffId == null) {
            throw new ShiftDomainException("Không thể xếp ca làm việc cho nhân viên không có mã định danh!");
        }
        if (this.shiftDate == null || this.startTime == null || this.endTime == null) {
            throw new ShiftDomainException("Thông tin ngày làm, giờ bắt đầu và giờ kết thúc ca bắt buộc phải điền đầy đủ!");
        }

        // Nghiệp vụ thực tế: Chặn không cho quản lý xếp ca lùi về quá khứ
        if (this.shiftDate.isBefore(LocalDate.now())) {
            throw new ShiftDomainException("Không được phép sắp xếp lịch ca làm việc cho một ngày trong quá khứ!");
        }

        // Nghiệp vụ thực tế: Giờ ra ca phải sau giờ vào ca
        if (!this.endTime.isAfter(this.startTime)) {
            throw new ShiftDomainException("Thời gian kết thúc ca làm việc phải sau thời gian bắt đầu vào ca!");
        }
    }

    /**
     * 🧠 RICH DOMAIN BUSINESS LOGIC 2: Kiểm tra xem một khung giờ đặt lịch của khách
     * có nằm trọn vẹn bên trong ca làm việc đăng ký của thợ này hay không.
     * * @param bookingStart Giờ khách hẹn đến
     * @param bookingEnd Giờ hệ thống tính toán thợ làm xong cho khách
     * @return true nếu thợ đang trong giờ làm việc, ngược lại trả về false.
     */
    public boolean isTimeRangeWithinShift(LocalTime bookingStart, LocalTime bookingEnd) {
        if (!this.active) {
            return false;
        }
        // Điều kiện nằm trọn trong ca: (bookingStart >= shiftStartTime) VÀ (bookingEnd <= shiftEndTime)
        return (bookingStart.equals(this.startTime) || bookingStart.isAfter(this.startTime))
                && (bookingEnd.equals(this.endTime) || bookingEnd.isBefore(this.endTime));
    }
}