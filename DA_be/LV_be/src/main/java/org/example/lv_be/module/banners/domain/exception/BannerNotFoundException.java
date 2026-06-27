package org.example.lv_be.module.banners.domain.exception;

public class BannerNotFoundException extends RuntimeException {

    public BannerNotFoundException(Long id) {
        super("Không tìm thấy Banner với ID: " + id);
    }

    public BannerNotFoundException(String message) {
        super(message);
    }
}