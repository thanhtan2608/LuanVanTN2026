package org.example.lv_be.module.users.domain.exception;

public class UserDomainException extends RuntimeException {
    public UserDomainException(String message) {
        super(message);
    }

    public UserDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}