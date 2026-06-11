package org.example.lv_be.module.booking.application.interfaces;
import org.example.lv_be.module.booking.application.dto.shift.ShiftResponse;
import java.time.LocalDate;
import java.util.Optional;
public interface IGetStaffShiftUseCase { Optional<ShiftResponse> execute(Long staffId, LocalDate date); }