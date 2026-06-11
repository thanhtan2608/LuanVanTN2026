package org.example.lv_be.module.booking.application.interfaces;
import org.example.lv_be.module.booking.application.dto.BookingDetailResponse;
public interface IGetBookingByIdUseCase { BookingDetailResponse execute(Long id); }