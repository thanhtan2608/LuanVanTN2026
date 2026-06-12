package org.example.lv_be.module.payroll.presentation.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.payroll.application.dto.request.CheckInRequest;
import org.example.lv_be.module.payroll.application.dto.request.OverrideAttendanceRequest;
import org.example.lv_be.module.payroll.application.dto.response.AttendanceResponse;
import org.example.lv_be.module.payroll.application.dto.response.TodayAttendanceBoardResponse;
import org.example.lv_be.module.payroll.application.interfaces.in.ICheckInUseCase;
import org.example.lv_be.module.payroll.application.interfaces.in.ICheckOutUseCase;
import org.example.lv_be.module.payroll.application.interfaces.in.IOverrideAttendanceUseCase;
import org.example.lv_be.module.payroll.application.interfaces.in.IGetTodayBoardUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller Quản lý Chấm công (Attendance)
 * 🔒 Quyền truy cập: Chỉ user có Role là MANAGER, RECEPTIONIST hoặc ADMIN mới gọi được các API này.
 * Frontend cần đính kèm Bearer Token vào Header khi gọi axios/fetch.
 */
@RestController
@RequestMapping("/api/v1/admin/attendances")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MANAGER', 'STAFF', 'ADMIN')")
public class AttendanceController {

    private final IGetTodayBoardUseCase getTodayBoardUseCase;
    private final ICheckInUseCase checkInUseCase;
    private final ICheckOutUseCase checkOutUseCase;
    private final IOverrideAttendanceUseCase overrideAttendanceUseCase;

    /**
     * [GET] /api/v1/admin/attendances/today
     * * 🎯 Mục đích: Lấy danh sách điểm danh của TẤT CẢ nhân viên trong ngày hôm nay.
     * 💻 Dành cho Frontend (Next.js):
     * - Gọi API này ở hàm useEffect (hoặc getServerSideProps) để render ra cái Bảng (Table) danh sách nhân viên.
     * - Response trả về là một Mảng (Array) các object `TodayAttendanceBoardResponse`.
     * - Dựa vào trường `status` để render UI:
     * + Nếu `status === 'ABSENT'`: Hiển thị nút [Check-in] màu xanh.
     * + Nếu `status !== 'ABSENT'` (tức là LATE, ON_TIME...): Hiển thị nút [Check-out] màu đỏ và giờ check-in thực tế.
     */
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<TodayAttendanceBoardResponse>>> getTodayBoard() {
        List<TodayAttendanceBoardResponse> data = getTodayBoardUseCase.execute();
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy bảng chấm công hôm nay thành công"));
    }

    /**
     * [POST] /api/v1/admin/attendances/check-in
     * * 🎯 Mục đích: Lễ tân bấm nút xác nhận nhân viên đã đến quán.
     * 💻 Dành cho Frontend (Next.js):
     * - Bắn request khi user click nút [Check-in] trên bảng.
     * - Payload (Body): { "staffId": 1 }
     * - UI/UX:
     * + Bọc hàm gọi API trong try-catch.
     * + Nếu thành công (HTTP 200): Bắn toast notification "Check-in thành công" và gọi lại API [GET] /today để refresh bảng.
     * + Nếu lỗi (HTTP 400 - "Nhân viên này đã được Check-in rồi"): Bắn toast lỗi màu đỏ.
     */
    @PostMapping("/check-in")
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkIn(@Valid @RequestBody CheckInRequest request) {
        AttendanceResponse data = checkInUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Điểm danh nhân viên thành công!"));
    }

    /**
     * [POST] /api/v1/admin/attendances/check-out/{staffId}
     * * 🎯 Mục đích: Lễ tân bấm nút xác nhận nhân viên tan làm.
     * 💻 Dành cho Frontend (Next.js):
     * - Truyền trực tiếp ID của nhân viên lên URL (Path Variable). Không cần gửi Body.
     * - Ví dụ gọi axios: `axios.post('/api/v1/admin/attendances/check-out/1')`
     * - UI/UX: Gọi thành công thì reload lại bảng dữ liệu để hiển thị cột giờ về (`checkOutTime`).
     */
    @PostMapping("/check-out/{staffId}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkOut(@PathVariable Long staffId) {
        AttendanceResponse data = checkOutUseCase.execute(staffId);
        return ResponseEntity.ok(ApiResponse.success(data, "Xác nhận nhân viên tan làm thành công!"));
    }

    /**
     * [PUT] /api/v1/admin/attendances/{attendanceId}/status
     * * 🎯 Mục đích: Quản lý can thiệp ghi đè trạng thái (VD: Thợ đi muộn nhưng có xin phép trước -> Đổi từ LATE sang EXCUSED để không bị trừ lương).
     * 💻 Dành cho Frontend (Next.js):
     * - Tham số URL (Path Variable): `attendanceId` (ID của bản ghi chấm công, LƯU Ý: Không phải ID nhân viên).
     * - Payload (Body): { "newStatus": "EXCUSED" }
     * + Các giá trị Enum hợp lệ: "ON_TIME", "LATE", "ABSENT", "EXCUSED"
     * - UI/UX: Thường làm một cái Modal nhỏ hiện lên khi click vào chữ trạng thái ở cột Status, có một cái Dropdown (Select) để chọn trạng thái mới, bấm Lưu thì gọi API này.
     */
    @PutMapping("/{attendanceId}/status")
    public ResponseEntity<ApiResponse<AttendanceResponse>> overrideStatus(
            @PathVariable Long attendanceId,
            @Valid @RequestBody OverrideAttendanceRequest request) {
        AttendanceResponse data = overrideAttendanceUseCase.execute(attendanceId, request);
        return ResponseEntity.ok(ApiResponse.success(data, "Cập nhật trạng thái chấm công thành công"));
    }
}