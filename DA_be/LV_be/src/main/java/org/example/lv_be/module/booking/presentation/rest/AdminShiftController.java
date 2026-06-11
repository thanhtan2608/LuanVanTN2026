package org.example.lv_be.module.booking.presentation.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.booking.application.dto.shift.CreateShiftRequest;
import org.example.lv_be.module.booking.application.dto.shift.ShiftResponse;
import org.example.lv_be.module.booking.application.interfaces.ICreateShiftUseCase;
import org.example.lv_be.module.booking.application.interfaces.IGetStaffShiftUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 🔐 QUẢN LÝ CA LÀM VIỆC (ADMIN ONLY)
 */
@RestController
@RequestMapping("/api/v1/admin/shifts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminShiftController {

    private final ICreateShiftUseCase createShiftUseCase;
    private final IGetStaffShiftUseCase getStaffShiftUseCase;

    /**
     * 📘 FRONTEND NOTE: XẾP CA LÀM VIỆC CHO THỢ (STYLIST)
     * ------------------------------------------------------------------------
     * 🔹 URL: POST /api/v1/admin/shifts
     * 🔹 BODY: { "staffId": 3, "shiftDate": "2026-06-15", "startTime": "08:00:00", "endTime": "14:00:00" }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ShiftResponse>> createShift(@Valid @RequestBody CreateShiftRequest request) {
        ShiftResponse data = createShiftUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Phân ca làm việc cho nhân viên thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XEM LỊCH TRỰC CỦA MỘT NHÂN VIÊN TRONG NGÀY
     * ------------------------------------------------------------------------
     * 🔹 URL: GET /api/v1/admin/shifts/staff/{staffId}?date=2026-06-15
     */
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<ApiResponse<ShiftResponse>> getStaffShift(
            @PathVariable Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Optional<ShiftResponse> data = getStaffShiftUseCase.execute(staffId, date);
        return data.map(shift -> ResponseEntity.ok(ApiResponse.success(shift, "Tải thông tin ca làm việc thành công!")))
                .orElseGet(() -> ResponseEntity.ok(ApiResponse.success(null, "Nhân viên chưa có lịch trực vào ngày này.")));
    }
}