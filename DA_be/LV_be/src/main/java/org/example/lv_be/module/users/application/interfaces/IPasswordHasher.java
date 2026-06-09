package org.example.lv_be.module.users.application.interfaces;

public interface IPasswordHasher { // Đổi tên thành IPasswordHasher cho thuần logic
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}