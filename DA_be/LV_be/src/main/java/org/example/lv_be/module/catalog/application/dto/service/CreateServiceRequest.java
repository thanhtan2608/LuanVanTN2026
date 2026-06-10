package org.example.lv_be.module.catalog.application.dto.service;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateServiceRequest {
    @NotNull(message = "Mã danh mục không được để trống")
    private Long categoryId;
    @NotBlank(message = "Tên dịch vụ không được để trống")
    private String name;
    @Min(value = 0, message = "Giá dịch vụ không được âm")
    private double price;
    @Min(value = 1, message = "Thời gian thực hiện phải lớn hơn 0 phút")
    private int durationMinutes;
}