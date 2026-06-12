package org.example.lv_be.module.payroll.domain.entity;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommissionLog {
    private Long id;
    private Long staffId;
    private Long invoiceId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}