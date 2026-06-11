package org.example.lv_be.module.booking.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.booking.application.interfaces.IDeleteBookingUseCase;
import org.example.lv_be.module.booking.domain.entity.Booking;
import org.example.lv_be.module.booking.domain.exception.BookingDomainException;
import org.example.lv_be.module.booking.domain.repository.IBookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteBookingUseCaseImpl implements IDeleteBookingUseCase {

    private final IBookingRepository bookingRepository;

    @Override
    @Transactional
    public void execute(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingDomainException("Không tìm thấy dữ liệu cuộc hẹn cần hủy bỏ!"));

        booking.cancel(); // Gọi logic kiểm tra vòng đời của Domain trước khi chuyển trạng thái CANCELLED
        bookingRepository.sourceSave(booking);
    }
}