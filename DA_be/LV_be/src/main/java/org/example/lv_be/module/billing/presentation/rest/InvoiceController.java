package org.example.lv_be.module.billing.presentation.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.billing.application.dto.request.CreateInvoiceRequest;
import org.example.lv_be.module.billing.application.dto.response.InvoiceResponse;
import org.example.lv_be.module.billing.application.dto.response.PaymentUrlResponse;
import org.example.lv_be.module.billing.application.interfaces.in.ICreateInvoiceUseCase;
import org.example.lv_be.module.billing.application.interfaces.in.ICreateMomoPaymentUseCase;
import org.example.lv_be.module.billing.application.interfaces.in.IProcessCashPaymentUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/invoices")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')") // Chỉ nhân viên mới được thao tác
public class InvoiceController {

    private final ICreateInvoiceUseCase createInvoiceUseCase;
    private final IProcessCashPaymentUseCase processCashPaymentUseCase;
    private final ICreateMomoPaymentUseCase createMomoPaymentUseCase;

    /**
     * 1. 📝 TẠO HÓA ĐƠN NHÁP (TẠM TÍNH)
     * -------------------------------------------------------------------------
     * 👨‍💻 [DÀNH CHO FRONTEND]:
     * - Khi nào gọi?: Khi thợ làm xong dịch vụ, Lễ tân gom các dịch vụ/sản phẩm lại và bấm "Tạo hóa đơn".
     * - Đầu vào (Body): Gửi danh sách các ID dịch vụ, số lượng, mã giảm giá (nếu có).
     * - Xử lý Đầu ra: BE sẽ trả về chi tiết hóa đơn gồm Tổng tiền, VAT,... và quan trọng nhất là `invoiceId`.
     * -> FE CẦN LƯU LẠI `invoiceId` NÀY ĐỂ DÙNG CHO BƯỚC CHỌN PHƯƠNG THỨC THANH TOÁN BÊN DƯỚI.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<InvoiceResponse>> createInvoice(
            @Valid @RequestBody CreateInvoiceRequest request) {
        InvoiceResponse data = createInvoiceUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Đã tạo hóa đơn tạm tính thành công."));
    }

    /**
     * 2. 💵 THANH TOÁN TIỀN MẶT
     * -------------------------------------------------------------------------
     * 👨‍💻 [DÀNH CHO FRONTEND]:
     * - Khi nào gọi?: Khi khách đưa tiền mặt, Lễ tân kiểm tiền đủ, cất vào két và bấm nút "Xác nhận thu tiền".
     * - Đầu vào (URL): Truyền `invoiceId` (lấy từ bước 1) vào URL.
     * - Xử lý Đầu ra: Nhận HTTP 200 OK -> FE hiển thị Toast message "Thanh toán thành công",
     * đóng popup hóa đơn và load lại danh sách hóa đơn.
     */
    @PostMapping("/{id}/pay/cash")
    public ResponseEntity<ApiResponse<Void>> payWithCash(@PathVariable Long id) {
        processCashPaymentUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Thanh toán tiền mặt thành công. Đã đóng ca và trừ kho!"));
    }

    /**
     * 3. 📱 THANH TOÁN QUA VÍ MOMO (TẠO MÃ QR)
     * -------------------------------------------------------------------------
     * 👨‍💻 [DÀNH CHO FRONTEND]:
     * - Khi nào gọi?: Khi khách chọn quẹt MoMo, Lễ tân bấm nút "Thanh toán MoMo".
     * - Đầu vào (URL): Truyền `invoiceId` (lấy từ bước 1) vào URL.
     * - Xử lý Đầu ra: BE trả về object có chứa trường `payUrl` (Đường link thanh toán của MoMo).
     * * 💡 HƯỚNG DẪN HIỂN THỊ TRÊN MÀN HÌNH LỄ TÂN:
     * - Cách 1 (Mở tab mới): Mở `payUrl` sang một tab mới để hiển thị giao diện của MoMo.
     * - Cách 2 (Khuyên dùng cho POS): Dùng thư viện tạo QR Code (ví dụ: `qrcode.react`)
     * để biến chuỗi `payUrl` thành hình ảnh Mã QR ngay trên màn hình máy POS. Khách hàng chỉ
     * việc mở app MoMo trên điện thoại quét vào màn hình máy POS là xong.
     */
    @PostMapping("/{id}/pay/momo")
    public ResponseEntity<ApiResponse<PaymentUrlResponse>> generateMomoQrCode(@PathVariable Long id) {
        PaymentUrlResponse data = createMomoPaymentUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(data, "Đã tạo mã thanh toán Momo. Vui lòng cho khách quét QR."));
    }
}