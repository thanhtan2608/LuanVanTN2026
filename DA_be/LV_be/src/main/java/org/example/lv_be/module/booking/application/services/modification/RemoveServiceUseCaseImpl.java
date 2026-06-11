package org.example.lv_be.module.booking.application.services.modification;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.BookingStatus;
import org.example.lv_be.module.catalog.domain.repository.IServiceRepository;
import org.example.lv_be.module.booking.application.interfaces.IRemoveServiceUseCase;
import org.example.lv_be.module.booking.domain.entity.Booking;
import org.example.lv_be.module.booking.domain.entity.BookingService;
import org.example.lv_be.module.booking.domain.exception.BookingDomainException;
import org.example.lv_be.module.booking.domain.repository.IBookingRepository;
import org.example.lv_be.module.booking.domain.repository.IBookingServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RemoveServiceUseCaseImpl implements IRemoveServiceUseCase {

    private final IBookingRepository bookingRepository;
    private final IBookingServiceRepository bookingServiceRepository;
    private final IServiceRepository catalogServiceRepository;

    @Override
    @Transactional
    public void execute(Long bookingId, Long serviceId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingDomainException("Cuộc hẹn yêu cầu giảm dịch vụ không tồn tại!"));

        if (booking.getStatus() != BookingStatus.CHECKED_IN && booking.getStatus() != BookingStatus.IN_PROGRESS) {
            throw new BookingDomainException("Chỉ cho phép sửa giảm dịch vụ khi khách hàng đang ngồi tại quán!");
        }

        List<BookingService> linkedServices = bookingServiceRepository.findByBookingId(bookingId);
        if (linkedServices.size() <= 1) {
            throw new BookingDomainException("Không thể bớt dịch vụ! Cuộc hẹn bắt buộc phải giữ lại tối thiểu 1 dịch vụ cốt lõi.");
        }

        // Tìm bản ghi lưu vết trong booking_services
        BookingService targetLink = linkedServices.stream()
                .filter(s -> s.getServiceId().equals(serviceId))
                .findFirst()
                .orElseThrow(() -> new BookingDomainException("Dịch vụ này không nằm trong danh mục đã chọn của cuộc hẹn!"));

        // Gọi sang Catalog bốc số phút phục vụ để co ngắn giờ kết thúc của thợ
        int durationToSubtract = catalogServiceRepository.findById(serviceId)
                .map(org.example.lv_be.module.catalog.domain.entity.ServiceItem::getDurationMinutes)
                .orElse(0);

        LocalTime currentEndTime = booking.getEndTime();
        booking.setEndTime(currentEndTime.minusMinutes(durationToSubtract));

        // Tiến hành xóa ngầm bản ghi (Ở hạ tầng chúng ta sẽ viết câu lệnh delete thô bằng Spring Data JPA)
        // Lưu ý logic: Tại đây chúng ta gọi hạ tầng clear cặp liên hợp này.
        // Để đơn giản, ta tái sử dụng cơ chế xóa của JpaRepository thông qua Adapter ở bước sau.
        bookingRepository.sourceSave(booking);
    }
}