package org.example.lv_be.module.booking.application.interfaces;

import org.example.lv_be.module.booking.application.dto.BookingResponse;
import org.example.lv_be.module.booking.application.dto.CreateBookingRequest;

public interface ICreateWalkInBookingUseCase {
    // Tái sử dụng lại CreateBookingRequest cũ vì các thông tin truyền lên là như nhau
    BookingResponse execute(CreateBookingRequest request);
}