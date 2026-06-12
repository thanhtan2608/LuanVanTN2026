package org.example.lv_be.module.billing.domain.entity;

import lombok.*;
import org.example.lv_be.module.billing.domain.enums.ItemType;
import org.example.lv_be.module.billing.domain.exception.BillingDomainException;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItem {
    private Long id;
    private Long invoiceId;

    private ItemType itemType; // Enum: SERVICE hoặc PRODUCT
    private Long itemId;       // Trỏ tới ID của dịch vụ hoặc sản phẩm (khớp bảng DB)

    @Builder.Default
    private int quantity = 1;  // Mặc định là 1 theo schema
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private Long staffId;
    private BigDecimal commissionAmount;

    // 🧠 LOGIC NGHIỆP VỤ: Tự động tính toán thành tiền của dòng này
    public void calculateSubtotal() {
        if (this.quantity <= 0) {
            throw new BillingDomainException("Số lượng dịch vụ/sản phẩm phải lớn hơn 0.");
        }
        if (this.unitPrice == null || this.unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BillingDomainException("Đơn giá không hợp lệ.");
        }

        // subtotal = quantity * unit_price
        this.subtotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }
}