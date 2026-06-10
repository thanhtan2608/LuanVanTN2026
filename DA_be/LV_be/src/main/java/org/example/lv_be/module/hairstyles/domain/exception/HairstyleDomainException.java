package org.example.lv_be.module.hairstyles.domain.exception;

/**
 * Ngoại lệ nghiệp vụ lõi của phân hệ kiểu tóc và liên kết dịch vụ salon.
 */
public class HairstyleDomainException extends RuntimeException {
    public HairstyleDomainException(String message) {
        super(message);
    }
}