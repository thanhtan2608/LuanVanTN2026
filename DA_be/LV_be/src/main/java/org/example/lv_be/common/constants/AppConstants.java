package org.example.lv_be.common.constants;

public class AppConstants {
    // Pagination (Phân trang mặc định)
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";

    // Logic Tích điểm (Ví dụ: 10,000 VNĐ = 1 điểm)
    public static final double POINTS_CONVERSION_RATE = 10000.0;

    // Yêu cầu hủy lịch trước bao nhiêu giờ (Ví dụ: 2 giờ)
    public static final int CANCEL_ALLOWED_HOURS_BEFORE = 2;

    // Tiền tố cho Booking Code
    public static final String BOOKING_CODE_PREFIX = "HC";

    // Khóa Constructor để ngăn việc khởi tạo class này
    private AppConstants() {}
}
