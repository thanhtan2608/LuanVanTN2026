package org.example.lv_be.module.billing.infrastructure.external.communication;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.BookingStatus;
import org.example.lv_be.module.billing.application.interfaces.out.IBookingClient;
// Import UseCase của module Booking
import org.example.lv_be.module.booking.application.interfaces.IUpdateBookingStatusUseCase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingClientImpl implements IBookingClient {

    private final IUpdateBookingStatusUseCase updateBookingStatusUseCase;

    @Override
    public void markBookingAsCompleted(Long bookingId) {
        // Đẩy trạng thái lịch hẹn sang COMPLETED
        updateBookingStatusUseCase.execute(bookingId, BookingStatus.COMPLETED);
    }
}