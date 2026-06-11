package org.example.lv_be.module.booking.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.BookingStatus;
import org.example.lv_be.module.booking.application.dto.AvailableSlotsResponse;
import org.example.lv_be.module.booking.application.interfaces.IGetAvailableSlotsUseCase;
import org.example.lv_be.module.booking.domain.entity.Booking;
import org.example.lv_be.module.booking.domain.entity.Shift;
import org.example.lv_be.module.booking.domain.repository.IBookingRepository;
import org.example.lv_be.module.booking.domain.repository.IShiftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAvailableSlotsUseCaseImpl implements IGetAvailableSlotsUseCase {

    private final IShiftRepository shiftRepository;
    private final IBookingRepository bookingRepository;

    @Override
    public AvailableSlotsResponse execute(Long staffId, LocalDate date) {
        // 🧠 THUẬT TOÁN ĐỈNH CAO CHẠY THẬT: Tìm ca trực thực tế trong bảng shifts
        Shift staffShift = shiftRepository.findByStaffIdAndShiftDate(staffId, date)
                .orElse(null);

        // Quy tắc thực tế: Nếu ngày hôm đó thợ không đăng ký ca trực -> Trả về mảng rỗng ngay lập tức
        if (staffShift == null || !staffShift.isActive()) {
            return AvailableSlotsResponse.builder()
                    .staffId(staffId)
                    .availableSlots(new ArrayList<>())
                    .build();
        }

        // Bốc khung thời gian động từ ca làm thực tế của thợ
        LocalTime openTime = staffShift.getStartTime();
        LocalTime closeTime = staffShift.getEndTime();

        List<LocalTime> possibleSlotsInShift = new ArrayList<>();
        LocalTime tempTime = openTime;
        while (tempTime.isBefore(closeTime)) {
            possibleSlotsInShift.add(tempTime);
            tempTime = tempTime.plusMinutes(30); // Cắt lát 30 phút một slot giờ
        }

        // Quét lịch bận trong DB của thợ
        List<Booking> bookedSchedules = bookingRepository.findByStaffIdAndBookingDate(staffId, date);
        List<String> validFreeSlots = new ArrayList<>();

        for (LocalTime slot : possibleSlotsInShift) {
            boolean isOverlap = false;
            for (Booking b : bookedSchedules) {
                if (b.getStatus() != BookingStatus.CANCELLED) {
                    // Nếu lát cắt giờ trùng hoặc nằm giữa ca làm bận b.getStartTime() và b.getEndTime() -> Loại bỏ
                    if ((slot.equals(b.getStartTime()) || slot.isAfter(b.getStartTime())) && slot.isBefore(b.getEndTime())) {
                        isOverlap = true;
                        break;
                    }
                }
            }
            if (!isOverlap) {
                validFreeSlots.add(slot.toString());
            }
        }

        return AvailableSlotsResponse.builder()
                .staffId(staffId)
                .availableSlots(validFreeSlots)
                .build();
    }
}