package org.example.lv_be.module.billing.domain.enums;

public enum InvoiceStatus {
    UNPAID,    // Chờ thanh toán
    PAID,       // Đã thanh toán hoàn tất
    REFUNDED    // Đã hoàn tiền cho khách
}