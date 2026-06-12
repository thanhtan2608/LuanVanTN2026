package org.example.lv_be.module.billing.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.lv_be.module.billing.domain.enums.ItemType;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreateInvoiceRequest {
    @NotNull(message = "Chi nhánh không được để trống")
    private Long branchId;

    private Long bookingId;  // Có thể null nếu khách vãng lai mua đồ rồi về
    private Long customerId; // Có thể null

    @NotEmpty(message = "Phải có ít nhất 1 dịch vụ/sản phẩm")
    private List<InvoiceItemRequest> items;

    @Getter
    @Setter
    public static class InvoiceItemRequest {
        @NotNull
        private ItemType itemType;
        @NotNull
        private Long itemId;
        @NotNull
        private Integer quantity;
        @NotNull
        private BigDecimal unitPrice; // Thực tế giá này nên được query lại từ DB để chống hack thay đổi giá từ Frontend
    }
}