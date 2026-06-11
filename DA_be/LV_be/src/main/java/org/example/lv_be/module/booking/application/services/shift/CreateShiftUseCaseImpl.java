package org.example.lv_be.module.booking.application.services.shift;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.booking.application.dto.shift.CreateShiftRequest;
import org.example.lv_be.module.booking.application.dto.shift.ShiftResponse;
import org.example.lv_be.module.booking.application.interfaces.ICreateShiftUseCase;
import org.example.lv_be.module.booking.application.mappers.ShiftMapper;
import org.example.lv_be.module.booking.domain.entity.Shift;
import org.example.lv_be.module.booking.domain.exception.ShiftDomainException;
import org.example.lv_be.module.booking.domain.repository.IShiftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateShiftUseCaseImpl implements ICreateShiftUseCase {

    private final IShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;

    @Override
    @Transactional
    public ShiftResponse execute(CreateShiftRequest request) {
        // Kiểm tra chống xếp trùng ca làm việc của cùng một thợ trong một ngày
        if (shiftRepository.existsByStaffIdAndShiftDate(request.getStaffId(), request.getShiftDate())) {
            throw new ShiftDomainException("Nhân viên này đã được phân bổ ca làm việc cho ngày " + request.getShiftDate() + " từ trước!");
        }

        Shift shift = shiftMapper.toDomain(request);
        shift.setActive(true);
        shift.validateSelf(); // Kích hoạt rich domain check giờ logic

        Shift savedShift = shiftRepository.sourceSave(shift);
        return shiftMapper.toResponse(savedShift);
    }
}