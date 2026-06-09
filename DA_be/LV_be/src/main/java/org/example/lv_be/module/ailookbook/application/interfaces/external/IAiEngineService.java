package org.example.lv_be.module.ailookbook.application.interfaces.external;

public interface IAiEngineService {
    /**
     * Gửi ảnh mặt thật + cấu hình Prompt sang AI Server bên thứ ba để render lồng ghép tóc
     */
    String generateHairStyle(String sourceImageUrl, String promptCommand);
}