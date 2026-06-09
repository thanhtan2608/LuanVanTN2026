package org.example.lv_be.module.ailookbook.domain.exception;

public class LookbookItemNotFoundException extends AiLookbookDomainException {
    public LookbookItemNotFoundException(Long id) {
        super("Không tìm thấy mẫu tóc AI với mã số: " + id);
    }
}