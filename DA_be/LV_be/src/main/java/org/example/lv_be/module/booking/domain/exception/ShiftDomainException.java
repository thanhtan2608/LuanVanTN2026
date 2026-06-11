package org.example.lv_be.module.booking.domain.exception;

/**
 * Ngoại lệ chuyên biệt để chặn các hành vi vi phạm quy tắc sắp xếp ca làm của Salon.
 */
public class ShiftDomainException extends RuntimeException {
    public ShiftDomainException(String message) {
        super(message);
    }
}