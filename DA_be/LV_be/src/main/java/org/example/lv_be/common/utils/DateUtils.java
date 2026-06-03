package org.example.lv_be.common.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Kiểm tra xem giờ A có bị trùng (overlap) với khoảng giờ [B - C] không
    public static boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private DateUtils() {}
}