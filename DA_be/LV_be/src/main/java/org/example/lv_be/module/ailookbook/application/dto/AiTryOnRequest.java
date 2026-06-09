package org.example.lv_be.module.ailookbook.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AiTryOnRequest {

    @NotNull(message = "Mã mẫu tóc AI bạn chọn thử không được để trống")
    private Long lookbookItemId;

    @NotNull(message = "Hình ảnh khuôn mặt chân dung để ghép không được để trống")
    private MultipartFile customerImage;
}