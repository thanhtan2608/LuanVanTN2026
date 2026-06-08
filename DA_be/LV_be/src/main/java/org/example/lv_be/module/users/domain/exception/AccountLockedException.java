package org.example.lv_be.module.users.domain.exception;

public class AccountLockedException extends UserDomainException {
    public AccountLockedException(String message) {
        super(message);
    }
}