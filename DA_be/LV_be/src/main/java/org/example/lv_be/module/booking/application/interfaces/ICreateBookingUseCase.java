package org.example.lv_be.module.booking.application.interfaces;
import org.example.lv_be.module.booking.application.dto.BookingResponse;
import org.example.lv_be.module.booking.application.dto.CreateBookingRequest;
public interface ICreateBookingUseCase { BookingResponse execute(CreateBookingRequest request); }