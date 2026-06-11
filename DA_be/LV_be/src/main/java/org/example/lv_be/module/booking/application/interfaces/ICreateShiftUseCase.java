package org.example.lv_be.module.booking.application.interfaces;
import org.example.lv_be.module.booking.application.dto.shift.CreateShiftRequest;
import org.example.lv_be.module.booking.application.dto.shift.ShiftResponse;
public interface ICreateShiftUseCase { ShiftResponse execute(CreateShiftRequest request); }