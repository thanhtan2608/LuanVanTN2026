package org.example.lv_be.module.booking.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.booking.application.dto.BookingResponse;
import org.example.lv_be.module.booking.application.interfaces.IGetBranchBookingsUseCase;
import org.example.lv_be.module.booking.application.mappers.BookingMapper;
import org.example.lv_be.module.booking.domain.repository.IBookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetBranchBookingsUseCaseImpl implements IGetBranchBookingsUseCase {

    private final IBookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public List<BookingResponse> execute(Long branchId, LocalDate date) {
        return bookingRepository.findByBranchIdAndBookingDate(branchId, date).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }
}