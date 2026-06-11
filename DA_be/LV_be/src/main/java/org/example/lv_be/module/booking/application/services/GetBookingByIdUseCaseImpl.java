package org.example.lv_be.module.booking.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.domain.repository.IServiceRepository;
import org.example.lv_be.module.hairstyles.domain.repository.IHairstyleRepository;
import org.example.lv_be.module.booking.application.dto.BookingDetailResponse;
import org.example.lv_be.module.booking.application.interfaces.IGetBookingByIdUseCase;
import org.example.lv_be.module.booking.domain.entity.Booking;
import org.example.lv_be.module.booking.domain.entity.BookingService;
import org.example.lv_be.module.booking.domain.exception.BookingDomainException;
import org.example.lv_be.module.booking.domain.repository.IBookingRepository;
import org.example.lv_be.module.booking.domain.repository.IBookingServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetBookingByIdUseCaseImpl implements IGetBookingByIdUseCase {

    private final IBookingRepository bookingRepository;
    private final IBookingServiceRepository bookingServiceRepository;
    private final IServiceRepository catalogServiceRepository; // Để bốc tên dịch vụ thật
    private final IHairstyleRepository hairstyleRepository;     // Để bốc tên mẫu tóc thật từ AI

    @Override
    public BookingDetailResponse execute(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingDomainException("Mã cuộc hẹn không tồn tại trên hệ thống!"));

        // Gọi chéo module lấy thông tin hiển thị bổ trợ
        String hairstyleName = "Không chọn mẫu trước";
        if (booking.getHairstyleId() != null) {
            hairstyleName = hairstyleRepository.findById(booking.getHairstyleId())
                    .map(org.example.lv_be.module.hairstyles.domain.entity.Hairstyle::getName)
                    .orElse("Mẫu tóc đã bị ẩn");
        }

        // Bốc mảng dịch vụ liên kết
        List<BookingService> mappedServices = bookingServiceRepository.findByBookingId(id);
        List<BookingDetailResponse.SelectedServiceDto> serviceDtos = new ArrayList<>();
        double calculatedPrice = 0;

        for (BookingService bs : mappedServices) {
            String serviceName = catalogServiceRepository.findById(bs.getServiceId())
                    .map(org.example.lv_be.module.catalog.domain.entity.ServiceItem::getName)
                    .orElse("Dịch vụ không rõ tên");

            serviceDtos.add(BookingDetailResponse.SelectedServiceDto.builder()
                    .serviceId(bs.getServiceId())
                    .serviceName(serviceName)
                    .priceAtBooking(bs.getPriceAtBooking())
                    .build());

            calculatedPrice += bs.getPriceAtBooking();
        }

        return BookingDetailResponse.builder()
                .bookingId(booking.getId())
                .code(booking.getCode())
                .branchId(booking.getBranchId())
                .branchName("Chi nhánh hệ thống Salon") // Đã giả định lấy từ core/branch tương ứng
                .staffName("Thợ Stylist ID: " + booking.getStaffId())
                .hairstyleName(hairstyleName)
                .bookingDate(booking.getBookingDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus())
                .notes(booking.getNotes())
                .totalPrice(calculatedPrice)
                .services(serviceDtos)
                .build();
    }
}