package org.example.lv_be.module.ailookbook.application.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LookbookDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String gender;
    private Long hairstyleId;
    private LocalDateTime createdAt;

    // MÓC NỐI KINH DOANH: Trả kèm danh sách bảng giá gói dịch vụ đời thật để Frontend hiển thị nút Đặt lịch
    private List<ActualServiceDto> actualServices;

    @Data
    public static class ActualServiceDto {
        private Long id;
        private String serviceName;
        private Double price;
        private Integer durationMinutes;
    }
}