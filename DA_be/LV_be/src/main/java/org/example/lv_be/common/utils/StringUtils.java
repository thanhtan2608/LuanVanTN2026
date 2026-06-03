package org.example.lv_be.common.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * Hàm loại bỏ dấu tiếng Việt (Ví dụ: "Lê Văn" -> "Le Van")
     */
    public static String removeAccents(String value) {
        if (value == null) return null;
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
        } catch (Exception ex) {
            return value;
        }
    }

    private StringUtils() {}
}
