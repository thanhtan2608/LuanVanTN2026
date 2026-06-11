package org.example.lv_be.module.booking.domain.entity;

import lombok.*;
import org.example.lv_be.common.enums.BookingStatus;
import org.example.lv_be.module.booking.domain.exception.BookingDomainException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    private Long id;                // bigint(20) AUTO_INCREMENT
    private String code;            // varchar(20) - Mã lịch hẹn (Ví dụ: BK8392)
    private Long branchId;          // bigint(20) -> cột branch_id
    private Long customerId;        // bigint(20) -> cột customer_id (Có thể null nếu khách vãng lai)
    private Long staffId;           // bigint(20) -> cột staff_id (Thợ làm tóc/Stylist đảm nhận)
    private Long hairstyleId;       // bigint(20) -> cột hairstyle_id (Kiểu tóc chọn từ AI Lookbook)
    private LocalDate bookingDate;  // date -> cột booking_date
    private LocalTime startTime;    // time -> cột start_time
    private LocalTime endTime;      // time -> cột end_time
    private BookingStatus status;    // enum('PENDING', 'CONFIRMED', 'CHECKED_IN', 'IN_PROGRESS',...)
    private String notes;           // text -> cột notes
    private Integer version;        // int(11) -> Quản lý tránh trùng slot ca làm
    private LocalDateTime createdAt;// timestamp

    /**
     * Ràng buộc nghiệp vụ tự validation dữ liệu lịch hẹn
     */
    public void validateSelf() {
        if (this.code == null || this.code.isBlank()) {
            throw new BookingDomainException("Mã lịch hẹn đặt chỗ bắt buộc phải có!");
        }
        if (this.branchId == null) {
            throw new BookingDomainException("Vui lòng chọn chi nhánh Salon để đến làm tóc!");
        }
        if (this.staffId == null) {
            throw new BookingDomainException("Lịch hẹn bắt buộc phải chỉ định một nhân viên phục vụ!");
        }
        if (this.bookingDate == null || this.startTime == null || this.endTime == null) {
            throw new BookingDomainException("Thông tin ngày hẹn và khung giờ bắt đầu/kết thúc không được trống!");
        }

        // Nghiệp vụ thực tế: Chặn không cho đặt lịch lùi về ngày trong quá khứ
        if (this.bookingDate.isBefore(LocalDate.now())) {
            throw new BookingDomainException("Không thể đặt lịch hẹn vào một ngày đã qua trong quá khứ!");
        }

        // Nghiệp vụ thực tế: Giờ kết thúc ca làm phải sau giờ bắt đầu
        if (!this.endTime.isAfter(this.startTime)) {
            throw new BookingDomainException("Thời gian kết thúc phục vụ phải sau thời gian bắt đầu làm!");
        }
    }

    /**
     * Nghiệp vụ 1: Xác nhận chốt lịch cho khách (PENDING -> CONFIRMED)
     */
    public void confirm() {
        if (this.status != BookingStatus.PENDING) {
            throw new BookingDomainException("Chỉ cho phép xác nhận những lịch hẹn đang ở trạng thái CHỜ XÁC NHẬN!");
        }
        this.status = BookingStatus.CONFIRMED;
    }

    /**
     * Nghiệp vụ 2: Khách đã đến tiệm và check-in tại quầy tiếp đón (CONFIRMED -> CHECKED_IN)
     */
    public void checkIn() {
        if (this.status != BookingStatus.CONFIRMED) {
            throw new BookingDomainException("Lịch hẹn chưa được xác nhận, không thể thực hiện CHECK-IN tiếp đón!");
        }
        this.status = BookingStatus.CHECKED_IN;
    }

    /**
     * Nghiệp vụ 3: Thợ làm tóc bắt đầu thực hiện kéo/hóa chất cho khách (CHECKED_IN -> IN_PROGRESS)
     */
    public void startService() {
        if (this.status != BookingStatus.CHECKED_IN) {
            throw new BookingDomainException("Khách hàng cần phải hoàn tất thủ tục CHECK-IN tại quầy trước khi thợ lên ghế làm tóc!");
        }
        this.status = BookingStatus.IN_PROGRESS;
    }

    /**
     * Nghiệp vụ 4: Hủy lịch hẹn làm tóc
     */
    public void cancel() {
        if (this.status == BookingStatus.IN_PROGRESS) {
            throw new BookingDomainException("Thợ đang thực hiện làm tóc cho khách, không thể hủy lịch hẹn ở giai đoạn này!");
        }
        // Cho phép hủy nếu đang PENDING hoặc CONFIRMED
        this.status = BookingStatus.CANCELLED;
    }
    public void complete() {
        // Chỉ hoàn thành khi thợ đang làm việc (IN_PROGRESS)
        if (this.status != BookingStatus.IN_PROGRESS) {
            throw new BookingDomainException("Chỉ có thể hoàn thành lịch chỗ khi thợ đang làm tóc (IN_PROGRESS)!");
        }
        this.status = BookingStatus.COMPLETED;
    }

    public void markAsNoShow() {
        // Chỉ đánh dấu bùng lịch nếu khách chưa đến tiệm (PENDING hoặc CONFIRMED)
        if (this.status != BookingStatus.PENDING && this.status != BookingStatus.CONFIRMED) {
            throw new BookingDomainException("Khách hàng đã đến tiệm hoặc lịch đã đóng, không thể đánh dấu bùng lịch!");
        }
        this.status = BookingStatus.NO_SHOW;
    }
}