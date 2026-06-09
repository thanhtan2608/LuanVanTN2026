package org.example.lv_be.module.branches.domain.exception;

public class BranchNotFoundException extends BranchDomainException {
    public BranchNotFoundException(Long id) {
        super("Không tìm thấy chi nhánh với mã định danh: " + id);
    }
}