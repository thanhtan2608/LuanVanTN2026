package org.example.lv_be.common.constants;

public class RegexConstants {
    // SĐT Việt Nam: Bắt đầu bằng 03, 05, 07, 08, 09 và có đúng 10 chữ số
    public static final String PHONE_VN_REGEX = "^(03|05|07|08|09)\\d{8}$";

    // Mật khẩu: Ít nhất 8 ký tự, có chữ và số
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    private RegexConstants() {}
}
