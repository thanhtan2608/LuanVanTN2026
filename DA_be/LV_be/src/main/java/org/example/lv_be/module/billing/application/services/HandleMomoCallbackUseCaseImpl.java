package org.example.lv_be.module.billing.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.billing.application.dto.request.MomoIpnCallbackRequest;
import org.example.lv_be.module.billing.application.interfaces.in.IHandleMomoCallbackUseCase;
import org.example.lv_be.module.billing.application.interfaces.out.*;
import org.example.lv_be.module.billing.domain.entity.Invoice;
import org.example.lv_be.module.billing.domain.entity.InvoiceItem;
import org.example.lv_be.module.billing.domain.enums.ItemType;
import org.example.lv_be.module.billing.domain.enums.PaymentMethod;
import org.example.lv_be.module.billing.domain.repository.IInvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal; // Thêm import BigDecimal

@Service
@RequiredArgsConstructor
public class HandleMomoCallbackUseCaseImpl implements IHandleMomoCallbackUseCase {

    private final IInvoiceRepository invoiceRepository;
    private final IMomoGatewayClient momoClient;
    private final IBookingClient bookingClient;
    private final IProductClient productClient;
    private final IUsersClient usersClient;
    private final IPayrollClient payrollClient;

    @Override
    @Transactional
    public void execute(MomoIpnCallbackRequest request) {
        // 1. Verify chữ ký điện tử (Chống giả mạo)
        if (!momoClient.verifyIpnSignature(request)) {
            throw new RuntimeException("Chữ ký Momo không hợp lệ! Nghi ngờ Request giả mạo.");
        }

        // 2. Lấy ID hóa đơn (Thường nhúng trong trường orderId, VD: "INV-100" -> split lấy 100)
        Long invoiceId = Long.parseLong(request.getOrderId().split("-")[1]);
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);

        if (invoice == null) return; // Nếu không tìm thấy, bỏ qua callback

        // 3. Xử lý theo mã trả về
        if (request.getResultCode() == 0) {
            try {
                // 3.1. Cập nhật trạng thái hóa đơn thành PAID và lưu DB
                invoice.markAsPaid(PaymentMethod.E_WALLET);
                invoiceRepository.sourceSave(invoice);

                // 3.2. Kích hoạt các hành động sau thanh toán (Tích điểm, trừ kho, hoàn tất lịch đặt)
                triggerPostPaymentActions(invoice);

                // 3.3. Bắn dữ liệu hoa hồng sang hệ thống Payroll
                for (InvoiceItem item : invoice.getItems()) {
                    if (item.getStaffId() != null && item.getCommissionAmount() != null
                            && item.getCommissionAmount().compareTo(BigDecimal.ZERO) > 0) {

                        payrollClient.sendCommissionData(item.getStaffId(), invoice.getId(), item.getCommissionAmount());
                    }
                }
            } catch (Exception e) {
                // Đã thanh toán trước đó rồi (bởi Lễ tân hoặc webhook gọi trùng) -> Bỏ qua
            }
        } else {
            // Thanh toán Momo thất bại (Khách hủy hoặc hết tiền)
            // Có thể ghi log hoặc cập nhật trạng thái FAILED (nếu có)
        }
    }

    private void triggerPostPaymentActions(Invoice invoice) {
        if (invoice.getBookingId() != null) bookingClient.markBookingAsCompleted(invoice.getBookingId());
        if (invoice.getCustomerId() != null) usersClient.addRewardPoints(invoice.getCustomerId(), invoice.getTotalAmount());
        invoice.getItems().stream()
                .filter(item -> item.getItemType() == ItemType.PRODUCT)
                .forEach(item -> productClient.deductInventory(item.getItemId(), item.getQuantity()));
    }
}