package org.example.lv_be.module.booking.application.interfaces;
import org.example.lv_be.module.booking.application.dto.BookingResponse;
import java.util.List;
public interface IGetCustomerBookingHistoryUseCase { List<BookingResponse> execute(Long customerId); }