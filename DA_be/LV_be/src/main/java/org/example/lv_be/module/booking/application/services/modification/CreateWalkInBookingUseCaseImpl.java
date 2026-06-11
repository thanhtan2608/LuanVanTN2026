package org.example.lv_be.module.booking.application.services.modification;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.BookingStatus;
import org.example.lv_be.module.catalog.domain.repository.IServiceRepository;
import org.example.lv_be.module.booking.application.dto.BookingResponse;
import org.example.lv_be.module.booking.application.dto.CreateBookingRequest;
import org.example.lv_be.module.booking.application.interfaces.ICreateWalkInBookingUseCase;
import org.example.lv_be.module.booking.application.mappers.BookingMapper;
import org.example.lv_be.module.booking.domain.entity.Booking;
import org.example.lv_be.module.booking.domain.entity.BookingService;
import org.example.lv_be.module.booking.domain.exception.BookingDomainException;
import org.example.lv_be.module.booking.domain.repository.IBookingRepository;
import org.example.lv_be.module.booking.domain.repository.IBookingServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateWalkInBookingUseCaseImpl implements ICreateWalkInBookingUseCase {

    private final IBookingRepository bookingRepository;
    private final IBookingServiceRepository bookingServiceRepository;
    private final IServiceRepository catalogServiceRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponse execute(CreateBookingRequest request) {
        int totalDuration = 0;
        List<BookingService> tempServicesList = new ArrayList<>();

        // 1. Quét Catalog tính tổng số phút làm tóc của các dịch vụ được chọn trực tiếp
        for (Long serviceId : request.getServiceIds()) {
            var serviceCatalog = catalogServiceRepository.findById(serviceId)
                    .orElseThrow(() -> new BookingDomainException("Dịch vụ ID " + serviceId + " không tồn tại!"));

            totalDuration += serviceCatalog.getDurationMinutes();
            tempServicesList.add(BookingService.builder()
                    .serviceId(serviceId)
                    .priceAtBooking(serviceCatalog.getPrice())
                    .build());
        }

        // 2. Lấy giờ lễ tân bấm máy làm giờ bắt đầu, tự tính giờ kết thúc dựa trên tổng số phút
        LocalTime startTime = request.getStartTime(); // Thường là LocalTime.now() từ Frontend gửi lên
        LocalTime endTime = startTime.plusMinutes(totalDuration);

        // 3. Kiểm tra xem thợ Stylist được chỉ định hiện tại có đang kẹt dở ca làm của khách khác không
        List<Booking> activeStaffBookings = bookingRepository.findByStaffIdAndBookingDate(request.getStaffId(), request.getBookingDate());
        for (Booking existing : activeStaffBookings) {
            if (existing.getStatus() != BookingStatus.CANCELLED && existing.getStatus() != BookingStatus.COMPLETED) {
                // Kiểm tra xung đột thời gian thực tế
                if (!(startTime.isAfter(existing.getEndTime()) || startTime.equals(existing.getEndTime()) ||
                        endTime.isBefore(existing.getStartTime()) || endTime.equals(existing.getStartTime()))) {
                    throw new BookingDomainException("Thợ hiện đang bận xử lý ca làm khác tại quán, vui lòng chọn thợ khác!");
                }
            }
        }

        // 4. Khởi tạo thực thể đơn đặt lịch vãng lai tại quầy
        Booking booking = bookingMapper.toDomain(request);
        booking.setCode("WK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()); // Tiền tố WK (Walk-in) để phân biệt
        booking.setEndTime(endTime);

        // 🌟 ĐIỂM SÁNG NGHIỆP VỤ: Khách đang đứng ở quầy nên nhảy thẳng vào trạng thái CHECKED_IN luôn!
        booking.setStatus(BookingStatus.CHECKED_IN);
        booking.setVersion(0);
        booking.setCreatedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.sourceSave(booking);

        // 5. Lưu vết danh sách dịch vụ làm tóc găm giá tiền
        for (BookingService bs : tempServicesList) {
            bs.setBookingId(savedBooking.getId());
            bs.validateSelf();
        }
        bookingServiceRepository.sourceSaveAll(tempServicesList);

        return bookingMapper.toResponse(savedBooking);
    }
}