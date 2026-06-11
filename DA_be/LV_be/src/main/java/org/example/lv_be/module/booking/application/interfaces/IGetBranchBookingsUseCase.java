package org.example.lv_be.module.booking.application.interfaces;
import org.example.lv_be.module.booking.application.dto.BookingResponse;
import java.time.LocalDate;
import java.util.List;
public interface IGetBranchBookingsUseCase { List<BookingResponse> execute(Long branchId, LocalDate date); }