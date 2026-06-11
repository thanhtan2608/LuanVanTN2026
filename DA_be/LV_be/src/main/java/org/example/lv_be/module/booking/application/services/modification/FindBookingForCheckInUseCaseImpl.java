package org.example.lv_be.module.booking.application.services.modification;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.booking.application.dto.BookingResponse;
import org.example.lv_be.module.booking.application.interfaces.IFindBookingForCheckInUseCase;
import org.example.lv_be.module.booking.application.interfaces.IUserClient; // Inject cầu nối module Users
import org.example.lv_be.module.booking.application.mappers.BookingMapper;
import org.example.lv_be.module.booking.domain.repository.IBookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindBookingForCheckInUseCaseImpl implements IFindBookingForCheckInUseCase {

    private final IBookingRepository bookingRepository;
    private final IUserClient userClient; // Gọi sang module Users của bạn
    private final BookingMapper bookingMapper;

    @Override
    public List<BookingResponse> execute(String phone) {
        // Bước 1: Gọi sang module Users hỏi xem số điện thoại này có User ID là bao nhiêu
        return userClient.findUserIdByPhone(phone.trim())
                // Bước 2: Bốc User ID đó map vào cột customer_id trong bảng bookings để lấy lịch hẹn
                .map(userId -> bookingRepository.findByCustomerId(userId).stream()
                        .map(bookingMapper::toResponse)
                        .toList())
                // Nếu số điện thoại này chưa có tài khoản trên hệ thống, trả về danh sách rỗng
                .orElse(new ArrayList<>());
    }
}