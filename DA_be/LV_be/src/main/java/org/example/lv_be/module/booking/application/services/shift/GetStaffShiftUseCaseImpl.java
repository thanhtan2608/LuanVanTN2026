package org.example.lv_be.module.booking.application.services.shift;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.booking.application.dto.shift.ShiftResponse;
import org.example.lv_be.module.booking.application.interfaces.IGetStaffShiftUseCase;
import org.example.lv_be.module.booking.application.mappers.ShiftMapper;
import org.example.lv_be.module.booking.domain.repository.IShiftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetStaffShiftUseCaseImpl implements IGetStaffShiftUseCase {

    private final IShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;

    @Override
    public Optional<ShiftResponse> execute(Long staffId, LocalDate date) {
        return shiftRepository.findByStaffIdAndShiftDate(staffId, date)
                .map(shiftMapper::toResponse);
    }
}