package org.example.lv_be.module.users.domain.exception;

public class InvalidOtpException extends UserDomainException {
    public InvalidOtpException(String message) {
        super(message);
    }
}