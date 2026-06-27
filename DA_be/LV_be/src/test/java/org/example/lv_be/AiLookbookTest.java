package org.example.lv_be;

import org.example.lv_be.module.ailookbook.application.interfaces.external.IAiEngineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AiLookbookTest {

    @Autowired
    private IAiEngineService aiEngineService;

    @Test
    public void testGenerateHairStyle() {
        System.out.println(">>> Đang giả lập test gọi AI Google Imagen 3...");
        
        // Dùng một link ảnh công khai ngẫu nhiên (chỉ để xem AI có kết nối được không)
        // Nếu ảnh không phải mặt người, AI có thể từ chối nhưng vẫn chứng minh là API hoạt động.
        String sourceImageUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?q=80&w=200&auto=format&fit=crop"; 
        String promptCommand = "Nhuộm tóc màu đỏ rực rỡ";

        try {
            System.out.println(">>> Đang đẩy dữ liệu sang siêu máy chủ Google AI Studio...");
            String base64Image = aiEngineService.generateHairStyle(sourceImageUrl, promptCommand);
            
            System.out.println("=========================================");
            System.out.println(">>> KẾT QUẢ TÍCH HỢP AI THÀNH CÔNG!");
            System.out.println(">>> Base64 Length: " + base64Image.length());
            System.out.println("=========================================");
        } catch (Exception e) {
            System.err.println(">>> KẾT QUẢ TÍCH HỢP AI THẤT BẠI: " + e.getMessage());
            throw e;
        }
    }
}
