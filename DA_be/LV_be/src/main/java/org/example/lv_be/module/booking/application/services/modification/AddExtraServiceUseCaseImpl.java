package org.example.lv_be.module.booking.application.services.modification;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.BookingStatus;
import org.example.lv_be.module.catalog.domain.repository.IServiceRepository;
import org.example.lv_be.module.booking.application.interfaces.IAddExtraServiceUseCase;
import org.example.lv_be.module.booking.domain.entity.Booking;
import org.example.lv_be.module.booking.domain.entity.BookingService;
import org.example.lv_be.module.booking.domain.exception.BookingDomainException;
import org.example.lv_be.module.booking.domain.repository.IBookingRepository;
import org.example.lv_be.module.booking.domain.repository.IBookingServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AddExtraServiceUseCaseImpl implements IAddExtraServiceUseCase {

    private final IBookingRepository bookingRepository;
    private final IBookingServiceRepository bookingServiceRepository;
    private final IServiceRepository catalogServiceRepository;

    @Override
    @Transactional
    public void execute(Long bookingId, Long serviceId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingDomainException("Cuộc hẹn phát sinh dịch vụ không tồn tại!"));

        // Ràng buộc thực tế: Chỉ được phát sinh khi khách đã ngồi lên ghế phục vụ
        if (booking.getStatus() != BookingStatus.CHECKED_IN && booking.getStatus() != BookingStatus.IN_PROGRESS) {
            throw new BookingDomainException("Chỉ cho phép thêm dịch vụ phát sinh khi khách hàng đang ở trạng thái CHECKED_IN hoặc IN_PROGRESS!");
        }

        // Gọi sang Catalog bốc dữ liệu thời gian và giá tiền thật
        var catalogService = catalogServiceRepository.findById(serviceId)
                .orElseThrow(() -> new BookingDomainException("Dịch vụ kỹ thuật muốn thêm không tồn tại trong danh mục hệ thống!"));

        // Thuật toán: Co dãn đẩy lùi giờ kết thúc ca làm của thợ
        LocalTime currentEndTime = booking.getEndTime();
        LocalTime newEndTime = currentEndTime.plusMinutes(catalogService.getDurationMinutes());
        booking.setEndTime(newEndTime);

        // Lưu vết liên kết mới vào bảng booking_services
        BookingService extraLink = BookingService.builder()
                .bookingId(bookingId)
                .serviceId(serviceId)
                .priceAtBooking(catalogService.getPrice())
                .build();

        extraLink.validateSelf();

        bookingServiceRepository.sourceSaveAll(java.util.List.of(extraLink));
        bookingRepository.sourceSave(booking); // Cập nhật lại thời gian tổng của cuộc hẹn
    }
}