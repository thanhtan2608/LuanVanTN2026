package org.example.lv_be.module.catalog.domain.exception;

/**
 * Ngoại lệ chuyên biệt để tóm bắt các hành vi vi phạm quy tắc kinh doanh
 * của riêng phân vùng danh mục, sản phẩm và dịch vụ salon.
 */
public class CatalogDomainException extends RuntimeException {
    public CatalogDomainException(String message) {
        super(message);
    }
}