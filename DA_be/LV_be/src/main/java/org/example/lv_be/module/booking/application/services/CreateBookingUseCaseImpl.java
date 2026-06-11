package org.example.lv_be.module.booking.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.BookingStatus;
import org.example.lv_be.module.catalog.domain.repository.IServiceRepository;
import org.example.lv_be.module.booking.application.dto.BookingResponse;
import org.example.lv_be.module.booking.application.dto.CreateBookingRequest;
import org.example.lv_be.module.booking.application.interfaces.ICreateBookingUseCase;
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
public class CreateBookingUseCaseImpl implements ICreateBookingUseCase {

    private final IBookingRepository bookingRepository;
    private final IBookingServiceRepository bookingServiceRepository;
    private final IServiceRepository catalogServiceRepository; // Kết nối chéo module Catalog lấy giá/số phút
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponse execute(CreateBookingRequest request) {
        // Thuật toán 1: Tính toán tổng thời gian và tổng tiền thực tế từ module Catalog
        int totalDuration = 0;
        double totalPrice = 0;
        List<BookingService> tempServicesList = new ArrayList<>();

        for (Long serviceId : request.getServiceIds()) {
            var serviceCatalog = catalogServiceRepository.findById(serviceId)
                    .orElseThrow(() -> new BookingDomainException("Dịch vụ ID " + serviceId + " không tồn tại trong hệ thống!"));

            totalDuration += serviceCatalog.getDurationMinutes();
            totalPrice += serviceCatalog.getPrice();

            tempServicesList.add(BookingService.builder()
                    .serviceId(serviceId)
                    .priceAtBooking(serviceCatalog.getPrice())
                    .build());
        }

        // Thuật toán 2: Tính toán khung giờ endTime
        LocalTime startTime = request.getStartTime();
        LocalTime endTime = startTime.plusMinutes(totalDuration);

        // Thuật toán 3: Kiểm tra xung đột lịch làm việc của Thợ (Chống trùng ca)
        List<Booking> activeStaffBookings = bookingRepository.findByStaffIdAndBookingDate(request.getStaffId(), request.getBookingDate());
        for (Booking existing : activeStaffBookings) {
            if (existing.getStatus() != BookingStatus.CANCELLED) {
                // Điều kiện chồng chéo khung giờ: !(Start1 >= End2 hoặc End1 <= Start2)
                if (!(startTime.isAfter(existing.getEndTime()) || startTime.equals(existing.getEndTime()) ||
                        endTime.isBefore(existing.getStartTime()) || endTime.equals(existing.getStartTime()))) {
                    throw new BookingDomainException("Thợ làm tóc đã bị kẹt lịch phục vụ trong khung giờ này!");
                }
            }
        }

        // Thuật toán 4: Khởi tạo và gán giá trị lõi cho Entity
        Booking booking = bookingMapper.toDomain(request);
        booking.setCode("BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        booking.setEndTime(endTime);
        booking.setStatus(BookingStatus.PENDING);
        booking.setVersion(0);
        booking.setCreatedAt(LocalDateTime.now());

        booking.validateSelf(); // Kích hoạt luật nghiệp vụ Domain chặn ngày quá khứ
        Booking savedBooking = bookingRepository.sourceSave(booking);

        // Thuật toán 5: Đồng bộ lưu vết mảng dịch vụ sang bảng liên hợp booking_services
        for (BookingService bs : tempServicesList) {
            bs.setBookingId(savedBooking.getId());
            bs.validateSelf();
        }
        bookingServiceRepository.sourceSaveAll(tempServicesList);

        return bookingMapper.toResponse(savedBooking);
    }
}