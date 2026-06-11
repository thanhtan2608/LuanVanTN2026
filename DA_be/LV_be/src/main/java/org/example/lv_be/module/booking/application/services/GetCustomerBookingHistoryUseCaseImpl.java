package org.example.lv_be.module.booking.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.booking.application.dto.BookingResponse;
import org.example.lv_be.module.booking.application.interfaces.IGetCustomerBookingHistoryUseCase;
import org.example.lv_be.module.booking.application.mappers.BookingMapper;
import org.example.lv_be.module.booking.domain.repository.IBookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCustomerBookingHistoryUseCaseImpl implements IGetCustomerBookingHistoryUseCase {

    private final IBookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public List<BookingResponse> execute(Long customerId) {
        // Lấy danh sách Booking (Domain) từ DB -> Stream qua Mapper chuyển thành BookingResponse (DTO)
        return bookingRepository.findByCustomerId(customerId).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }
}