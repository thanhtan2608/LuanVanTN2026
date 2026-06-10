package org.example.lv_be.module.catalog.application.dto.product;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReplenishStockRequest {
    @Min(value = 1, message = "Số lượng nhập kho bổ sung phải lớn hơn 0")
    private int quantity;
}