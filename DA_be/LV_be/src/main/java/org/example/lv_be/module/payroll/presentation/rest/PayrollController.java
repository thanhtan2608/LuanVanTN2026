package org.example.lv_be.module.payroll.presentation.rest;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.payroll.application.dto.response.PayrollResponse;
import org.example.lv_be.module.payroll.application.interfaces.in.ICalculatePayrollUseCase;
import org.example.lv_be.module.payroll.application.interfaces.in.IMarkPayrollAsPaidUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller Quản lý Lương thưởng (Payroll)
 * 🔒 Quyền truy cập: Chỉ user có Role là MANAGER hoặc ADMIN mới được phép xem và chốt lương.
 * Frontend cần đính kèm Bearer Token vào Header khi gọi axios/fetch.
 */
@RestController
@RequestMapping("/api/v1/admin/payrolls")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class PayrollController {

    private final ICalculatePayrollUseCase calculatePayrollUseCase;
    private final IMarkPayrollAsPaidUseCase markPayrollAsPaidUseCase;

    /**
     * [POST] /api/v1/admin/payrolls/calculate
     * * 🎯 Mục đích: Yêu cầu hệ thống tính toán (hoặc tính lại) bảng lương của 1 nhân viên trong 1 tháng cụ thể.
     * * 💻 Dành cho Frontend (Next.js):
     * - Cách truyền Data: KHÔNG truyền qua Body (JSON), mà truyền qua Query Parameters (URL).
     * - Ví dụ gọi Axios: `axios.post('/api/v1/admin/payrolls/calculate?staffId=1&month=5&year=2026')`
     * * - UI/UX Flow:
     * + B1: Trên màn hình Quản lý Lương có 2 bộ lọc (Filter): Chọn Tháng (Select) và Chọn Năm (Select).
     * + B2: Có một bảng danh sách nhân viên. Mỗi dòng có nút [Tính lương / Xem chi tiết].
     * + B3: Khi click nút đó, gọi API này.
     * + B4: Nhận Response (`PayrollResponse`), mở một Modal (Popup) hiển thị chi tiết:
     * Lương cứng (0đ) + Tổng hoa hồng (X đ) - Phạt đi muộn (Y đ) = Thực lĩnh (Z đ).
     * * 💡 Lưu ý cực hay cho đồ án: API này có thể bấm gọi đi gọi lại nhiều lần. Nếu quản lý lỡ châm chước sửa lỗi đi muộn cho thợ, chỉ cần bấm nút [Tính lại lương], gọi lại API này là con số sẽ cập nhật tức thì (miễn là trạng thái lương đang là UNPAID).
     */
    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<PayrollResponse>> calculatePayroll(
            @RequestParam Long staffId,
            @RequestParam int month,
            @RequestParam int year) {

        PayrollResponse data = calculatePayrollUseCase.execute(staffId, month, year);
        return ResponseEntity.ok(ApiResponse.success(data, "Tính toán bảng lương thành công!"));
    }

    /**
     * [PUT] /api/v1/admin/payrolls/{payrollId}/pay
     * * 🎯 Mục đích: Quản lý xác nhận đã chuyển khoản/trả tiền mặt cho thợ -> Khóa bảng lương lại.
     * * 💻 Dành cho Frontend (Next.js):
     * - Tham số URL (Path Variable): `payrollId` (Lấy từ trường `id` của response trả về ở API calculate phía trên).
     * - Ví dụ gọi Axios: `axios.put('/api/v1/admin/payrolls/15/pay')`
     * * - UI/UX Flow:
     * + Trong cái Modal chi tiết lương (ở bước trên), nếu `status === 'UNPAID'`, hiển thị nút [Xác nhận đã thanh toán] màu xanh.
     * + Quản lý bấm nút -> Nên hiện thêm 1 Modal Confirm ("Bạn có chắc chắn đã chuyển khoản cho nhân viên này? Thao tác này không thể hoàn tác!").
     * + Quản lý chọn "Đồng ý" -> Gọi API này.
     * + Nếu thành công (HTTP 200): Cập nhật lại UI, đổi Badge trạng thái thành PAID (Màu xanh lá) và ẨN/DISABLE nút thanh toán đi.
     */
    @PutMapping("/{payrollId}/pay")
    public ResponseEntity<ApiResponse<Void>> markAsPaid(@PathVariable Long payrollId) {
        markPayrollAsPaidUseCase.execute(payrollId);
        return ResponseEntity.ok(ApiResponse.success(null, "Đã xác nhận thanh toán bảng lương."));
    }
}