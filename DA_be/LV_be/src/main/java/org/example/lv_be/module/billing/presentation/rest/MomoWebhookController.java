package org.example.lv_be.module.billing.presentation.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lv_be.module.billing.application.dto.request.MomoIpnCallbackRequest;
import org.example.lv_be.module.billing.application.interfaces.in.IHandleMomoCallbackUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payment Webhooks", description = "Các API nhận thông báo kết quả thanh toán từ đối tác (MoMo, VNPay...)")
public class MomoWebhookController {

    private final IHandleMomoCallbackUseCase handleMomoCallbackUseCase;

    /**
     * 🤖 WEBHOOK MOMO TRẢ KẾT QUẢ VỀ (IPN - Instant Payment Notification)
     * ------------------------------------------------------------------------
     * LƯU Ý DÀNH CHO FRONTEND (FE):
     * - API NÀY FRONTEND KHÔNG BAO GIỜ ĐƯỢC GỌI.
     * - Đây là API Webhook Server-to-Server. Server của MoMo sẽ tự động gọi vào API này
     * (POST request) sau khi khách hàng thanh toán thành công (hoặc thất bại) trên app MoMo.
     * * FE cần làm gì sau khi khách thanh toán?
     * 1. Sau khi khách quét QR và thanh toán xong, trình duyệt của khách sẽ bị MoMo redirect
     * về trang Return URL (do FE cung cấp lúc tạo thanh toán).
     * 2. Tại trang Return URL đó, FE lấy mã `orderId` trên thanh URL (query param).
     * 3. FE gọi một API GET khác (ví dụ: /api/v1/invoices/{orderId}) để hỏi BE xem đơn hàng
     * này đã được cập nhật trạng thái PAID chưa (do Webhook này chạy ngầm phía sau).
     */
    @Operation(
            summary = "Webhook nhận kết quả thanh toán từ MoMo (IPN)",
            description = "API này dành riêng cho Server MoMo gọi vào. Frontend tuyệt đối không gọi API này."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Đã nhận và xử lý thành công (hoặc bỏ qua do đã xử lý trước đó)"),
            @ApiResponse(responseCode = "400", description = "Chữ ký không hợp lệ hoặc sai định dạng (Hacker gọi fake)")
    })
    @PostMapping("/momo-webhook")
    public ResponseEntity<Void> handleMomoIpn(@RequestBody MomoIpnCallbackRequest request) {
        log.info("🔔 Nhận được IPN (Webhook) từ MoMo cho mã đơn hàng: {}, Kết quả resultCode: {}",
                request.getOrderId(), request.getResultCode());

        try {
            // Ném vào UseCase để xử lý logic:
            // 1. Verify chữ ký bảo mật (signature).
            // 2. Kiểm tra resultCode (0 là thành công, khác 0 là thất bại).
            // 3. Cập nhật trạng thái Invoice (Hóa đơn) trong Database.
            handleMomoCallbackUseCase.execute(request);

            // Trả về 204 No Content cho Momo biết Server đã ghi nhận thành công,
            // MoMo không cần phải gọi lại Webhook này nữa.
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            log.error("❌ Lỗi khi xử lý Webhook MoMo cho đơn hàng {}: {}", request.getOrderId(), e.getMessage());
            // Trả về 400 Bad Request nếu chữ ký sai hoặc có lỗi logic nghiêm trọng.
            // Khi MoMo nhận HTTP 400, hệ thống của họ sẽ retry (gọi lại API này) sau vài phút.
            return ResponseEntity.badRequest().build();
        }
    }
}