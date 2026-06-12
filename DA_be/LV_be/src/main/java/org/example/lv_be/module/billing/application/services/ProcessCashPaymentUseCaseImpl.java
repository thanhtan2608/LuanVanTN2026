package org.example.lv_be.module.billing.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.billing.application.interfaces.in.IProcessCashPaymentUseCase;
import org.example.lv_be.module.billing.application.interfaces.out.IBookingClient;
import org.example.lv_be.module.billing.application.interfaces.out.IPayrollClient;
import org.example.lv_be.module.billing.application.interfaces.out.IProductClient;
import org.example.lv_be.module.billing.application.interfaces.out.IUsersClient;
import org.example.lv_be.module.billing.domain.entity.Invoice;
import org.example.lv_be.module.billing.domain.entity.InvoiceItem;
import org.example.lv_be.module.billing.domain.enums.ItemType;
import org.example.lv_be.module.billing.domain.enums.PaymentMethod;
import org.example.lv_be.module.billing.domain.exception.BillingDomainException;
import org.example.lv_be.module.billing.domain.repository.IInvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal; // Đã thêm import này

@Service
@RequiredArgsConstructor
public class ProcessCashPaymentUseCaseImpl implements IProcessCashPaymentUseCase {

    private final IInvoiceRepository invoiceRepository;
    private final IBookingClient bookingClient;
    private final IProductClient productClient;
    private final IUsersClient usersClient;
    private final IPayrollClient payrollClient;

    @Override
    @Transactional
    public void execute(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new BillingDomainException("Không tìm thấy hóa đơn"));

        // 1. Gọi Domain Logic chốt trạng thái
        invoice.markAsPaid(PaymentMethod.CASH);
        invoiceRepository.sourceSave(invoice);

        // 2. Kích hoạt Post-Payment Actions (Cực kỳ quan trọng)
        triggerPostPaymentActions(invoice);

        // 3. Ghi nhận tiền hoa hồng
        for (InvoiceItem item : invoice.getItems()) {
            // Kiểm tra xem hạng mục này có người thực hiện và có tiền hoa hồng không
            if (item.getStaffId() != null && item.getCommissionAmount() != null
                    && item.getCommissionAmount().compareTo(BigDecimal.ZERO) > 0) {

                // Gọi API nội bộ sang Payroll
                payrollClient.sendCommissionData(item.getStaffId(), invoice.getId(), item.getCommissionAmount());
            }
        }
    } // Đã xóa đi 1 dấu ngoặc nhọn bị dư ở đây

    private void triggerPostPaymentActions(Invoice invoice) {
        if (invoice.getBookingId() != null) {
            bookingClient.markBookingAsCompleted(invoice.getBookingId());
        }
        if (invoice.getCustomerId() != null) {
            usersClient.addRewardPoints(invoice.getCustomerId(), invoice.getTotalAmount());
        }
        invoice.getItems().stream()
                .filter(item -> item.getItemType() == ItemType.PRODUCT)
                .forEach(item -> productClient.deductInventory(item.getItemId(), item.getQuantity()));
    }
}