package org.example.lv_be.module.booking.application.interfaces;
import org.example.lv_be.common.enums.BookingStatus;
import org.example.lv_be.module.booking.application.dto.BookingResponse;

public interface IUpdateBookingStatusUseCase { BookingResponse execute(Long id, BookingStatus status); }