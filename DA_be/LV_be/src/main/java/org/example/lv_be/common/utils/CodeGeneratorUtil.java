package org.example.lv_be.common.utils;

import org.example.lv_be.common.constants.AppConstants;

import java.security.SecureRandom;

public class CodeGeneratorUtil {
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Sinh mã lịch hẹn (VD: HC-8A92B)
     */
    public static String generateBookingCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return AppConstants.BOOKING_CODE_PREFIX + "-" + sb.toString();
    }

    /**
     * Sinh mã OTP 6 số
     */
    public static String generateOTP() {
        int otp = 100000 + RANDOM.nextInt(900000);
        return String.valueOf(otp);
    }
    private CodeGeneratorUtil() {}
}