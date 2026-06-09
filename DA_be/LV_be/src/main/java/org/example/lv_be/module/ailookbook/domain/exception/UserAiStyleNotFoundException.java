package org.example.lv_be.module.ailookbook.domain.exception;

public class UserAiStyleNotFoundException extends AiLookbookDomainException {
    public UserAiStyleNotFoundException(Long id) {
        super("Không tìm thấy lịch sử thử tóc AI với mã số: " + id);
    }
}