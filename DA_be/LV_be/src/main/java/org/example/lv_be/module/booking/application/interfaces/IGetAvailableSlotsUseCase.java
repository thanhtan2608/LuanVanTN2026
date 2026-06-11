package org.example.lv_be.module.booking.application.interfaces;
import org.example.lv_be.module.booking.application.dto.AvailableSlotsResponse;
import java.time.LocalDate;
public interface IGetAvailableSlotsUseCase { AvailableSlotsResponse execute(Long staffId, LocalDate date); }