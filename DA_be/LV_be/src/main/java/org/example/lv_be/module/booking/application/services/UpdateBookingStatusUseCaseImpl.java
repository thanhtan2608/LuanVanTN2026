package org.example.lv_be.module.booking.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.BookingStatus;
import org.example.lv_be.module.booking.application.dto.BookingResponse;
import org.example.lv_be.module.booking.application.interfaces.IUpdateBookingStatusUseCase;
import org.example.lv_be.module.booking.application.mappers.BookingMapper;
import org.example.lv_be.module.booking.domain.entity.Booking;
import org.example.lv_be.module.booking.domain.exception.BookingDomainException;
import org.example.lv_be.module.booking.domain.repository.IBookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateBookingStatusUseCaseImpl implements IUpdateBookingStatusUseCase {

    private final IBookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponse execute(Long id, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingDomainException("Không tìm thấy đơn đặt lịch yêu cầu!"));

        // Điều phối hành vi dựa theo máy trạng thái Rich Domain Model
        switch (newStatus) {
            case CONFIRMED -> booking.confirm();
            case CHECKED_IN -> booking.checkIn();
            case IN_PROGRESS -> booking.startService();
            case CANCELLED -> booking.cancel();
            case COMPLETED -> booking.complete();
            case NO_SHOW -> booking.markAsNoShow();
        }

        return bookingMapper.toResponse(bookingRepository.sourceSave(booking));
    }
}