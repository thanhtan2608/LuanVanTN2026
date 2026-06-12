package org.example.lv_be.module.billing.domain.entity;

import lombok.*;
import org.example.lv_be.module.billing.domain.enums.InvoiceStatus;
import org.example.lv_be.module.billing.domain.enums.PaymentMethod;
import org.example.lv_be.module.billing.domain.exception.BillingDomainException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    private Long id;
    private String code;            // Mã hóa đơn (VD: INV-100234)
    private Long bookingId;         // Liên kết đến đơn đặt lịch (có thể null nếu khách vãng lai chỉ mua sản phẩm)
    private Long customerId;        // ID khách hàng để còn tích điểm
    private Long staffId;           // ID thu ngân hoặc thợ chốt bill

    private BigDecimal totalAmount;     // Tiền dịch vụ gốc
    private BigDecimal discountAmount;  // Tiền được giảm (Voucher / Hạng thành viên)
    private BigDecimal finalAmount;     // Số tiền thực tế khách phải trả

    private PaymentMethod paymentMethod;
    private InvoiceStatus status;
    private LocalDateTime createdAt;

    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();

    // 🧠 NGHIỆP VỤ 1: Tính toán tổng tiền hóa đơn dựa trên các món dịch vụ
    public void calculateFinalAmount() {
        if (items == null || items.isEmpty()) {
            throw new BillingDomainException("Hóa đơn phải có ít nhất 1 dịch vụ/sản phẩm!");
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (InvoiceItem item : items) {
            item.calculateSubtotal(); // Kích hoạt tính tiền từng dòng
            sum = sum.add(item.getSubtotal());
        }

        this.totalAmount = sum;

        // Kiểm tra tiền giảm giá
        if (discountAmount == null) discountAmount = BigDecimal.ZERO;
        if (discountAmount.compareTo(this.totalAmount) > 0) {
            this.discountAmount = this.totalAmount; // Khuyến mãi tối đa bằng tổng tiền
        }

        this.finalAmount = this.totalAmount.subtract(this.discountAmount);
    }

    // 🧠 NGHIỆP VỤ 2: Khóa dữ liệu khi đã thanh toán thành công
    public void markAsPaid(PaymentMethod method) {
        if (this.status == InvoiceStatus.PAID) {
            throw new BillingDomainException("Hóa đơn này đã được thanh toán rồi!");
        }
        this.paymentMethod = method;
        this.status = InvoiceStatus.PAID;
    }
}