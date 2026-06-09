package org.example.lv_be.module.ailookbook.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateLookbookRequest {
    @NotBlank(message = "Tiêu đề mẫu tóc AI không được để trống")
    private String title;

    @NotBlank(message = "Mô tả mẫu tóc AI không được để trống")
    private String description;

    @NotBlank(message = "Phân loại giới tính không được để trống (MALE, FEMALE, UNISEX)")
    private String gender;

    @NotBlank(message = "Câu lệnh Prompt cấu hình cho AI không được để trống")
    private String prompt;

    @NotNull(message = "Mã liên kết kiểu tóc đời thật không được để trống")
    private Long hairstyleId;

    @NotNull(message = "Trạng thái hiển thị không được để trống")
    private Boolean isActive;
}