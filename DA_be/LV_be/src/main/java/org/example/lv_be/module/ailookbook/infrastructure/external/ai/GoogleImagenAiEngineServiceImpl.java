package org.example.lv_be.module.ailookbook.infrastructure.external.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lv_be.module.ailookbook.application.interfaces.external.IAiEngineService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleImagenAiEngineServiceImpl implements IAiEngineService {

    private final RestTemplate aiRestTemplate;

    @Value("${app.ai.google.api-key}")
    private String googleApiKey;

    private static final String GOOGLE_IMAGEN_URL = "https://generativelanguage.googleapis.com/v1beta/models/imagen-3.0-generate-002:generateImages";

    @Override
    public String generateHairStyle(String sourceImageUrl, String promptCommand) {
        log.info("=== KIỂM TRA TÍNH HỢP LỆ CỦA CÂU LỆNH AI ===");

        // BƯỚC 1: Kích hoạt hàng rào bảo vệ - Chặn các câu lệnh không đúng chức năng làm tóc
        if (!isHairRelatedPrompt(promptCommand)) {
            log.warn("Cảnh báo: Khách hàng nhập câu lệnh sai chức năng hệ thống: '{}'", promptCommand);
            throw new RuntimeException("Hệ thống từ chối xử lý! Câu lệnh không đúng chức năng. Tính năng này chỉ chuyên dùng để thay đổi kiểu tóc, màu tóc hoặc tạo kiểu tóc.");
        }

        try {
            // BƯỚC 2: Tải ảnh từ ImageKit về Backend và mã hóa sang Base64
            log.info("Đang tải dữ liệu ảnh gốc từ ImageKit...");
            byte[] imageBytes = aiRestTemplate.getForObject(sourceImageUrl, byte[].class);
            if (imageBytes == null) {
                throw new RuntimeException("Không thể kết nối hoặc tải ảnh từ kho lưu trữ ImageKit!");
            }
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // BƯỚC 3: Ép khuôn Prompt kỹ thuật - Buộc AI chỉ được phép tương tác lên vùng tóc
            String tốiƯuPrompt = "A professional hair studio editing. Strictly modify ONLY the hair of the person to match this specific request: "
                    + promptCommand
                    + ". Mandatory constraints: Keep the exact same face identity, eyes, nose, mouth, facial features, expression, head posture, and background 100% unchanged. Do not alter anything except the hairstyle. High quality, realistic hair texture, photorealistic.";

            Map<String, Object> inlineData = Map.of(
                    "mimeType", "image/png",
                    "data", base64Image
            );

            Map<String, Object> requestBody = Map.of(
                    "prompt", tốiƯuPrompt,
                    "numberOfImages", 1,
                    "aspectRatio", "1:1",
                    "outputMimeType", "image/jpeg",
                    "imageContext", Map.of("inputImage", inlineData)
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // BƯỚC 4: Gửi request sang Google AI Studio
            String finalApiUrl = GOOGLE_IMAGEN_URL + "?key=" + googleApiKey;
            log.info("Đang đẩy dữ liệu sang siêu máy chủ Google AI Studio...");

            ResponseEntity<Map> response = aiRestTemplate.postForEntity(finalApiUrl, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            // BƯỚC 5: Trả ảnh kết quả dạng Base64 Data URL cho Frontend
            if (responseBody != null && responseBody.containsKey("generatedImages")) {
                var generatedImages = (List<Map<String, Object>>) responseBody.get("generatedImages");
                if (!generatedImages.isEmpty()) {
                    String resultBase64 = generatedImages.get(0).get("image").toString();
                    log.info("=== GOOGLE AI HOÀN THÀNH XỬ LÝ KIỂU TÓC THÀNH CÔNG! ===");
                    return "data:image/jpeg;base64," + resultBase64;
                }
            }

            throw new RuntimeException("Google AI Studio không trả về dữ liệu hình ảnh hợp lệ!");

        } catch (Exception e) {
            log.error("Lỗi tại lõi xử lý Google Imagen API: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Thuật toán quét và kiểm tra ngữ nghĩa từ khóa chuyên ngành tóc (Guardrail Filter)
     * Hỗ trợ cả tiếng Việt (có dấu/không dấu) và thuật ngữ tiếng Anh phổ biến.
     */
    private boolean isHairRelatedPrompt(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return false;
        }

        String lowerPrompt = prompt.toLowerCase();

        // Danh sách từ khóa bắt buộc phải xuất hiện ít nhất một từ trong câu lệnh
        List<String> hairKeywords = List.of(
                // === 1. HÀNH ĐỘNG / KỸ THUẬT LÀM TÓC (VIỆT NAM) ===
                "tóc", "toc", "mái", "mai", "gáy", "gay", "đầu", "dau", // Từ chỉ bộ phận
                "uốn", "uon", "duỗi", "duoi", "nhuộm", "nhuom", "cắt", "cat", "tỉa", "tia",
                "tẩy", "tay", "ép", "ep", "bấm", "bam", "dập", "dap", "phồng", "phong",
                "sấy", "say", "vuốt", "vuot", "chải", "chai", "tết", "tet", "búi", "bui",
                "bím", "bim", "nối", "noi", "cạo", "cao", "hớt", "hot", "gội", "goi",
                "xoăn", "xoan", "gợn", "gon", "sóng", "song", "bổ luống", "bo luong", "chẻ ngôi", "che ngoi",

                // === 2. KIỂU TÓC NAM PHỔ BIẾN (MESSY, SHORT, TRENDY) ===
                "layer", "undercut", "mullet", "side part", "sidepart", "buzz", "mohawk",
                "pompadour", "comma hair", "quiff", "fade", "mohican", "sport", "slickback",
                "slick back", "topknot", "top knot", "dreadlocks", "manbun", "man bun",
                "đầu đinh", "dau dinh", "đầu nấm", "dau nam", "hai mái", "hai mai", "bình dân", "binh dan",

                // === 3. KIỂU TÓC NỮ HOT TREND ===
                "bob", "pixie", "wolfcut", "wolf cut", "hime", "shaggy", "tém", "tem",
                "mái ngố", "mai ngo", "râu rồng", "rau rong", "mái thưa", "mai thua",
                "mái bay", "mai bay", "lá", "la", "ngang vai", "ngang vai", "uốn lơi", "uon loi",

                // === 4. BẢNG MÀU NHUỘM (COLOR & HIGHLIGHT PHỔ BIẾN) ===
                "khói", "khoi", "rêu", "reu", "bạch kim", "bach kim", "vàng", "vang",
                "đỏ", "do", "xanh", "xanh", "tím", "tim", "nâu", "nau", "đen", "den",
                "hạt dẻ", "hat de", "trà sữa", "tra sua", "bạc", "bac", "xám", "xam", "hồng", "hong",
                "ombre", "highlight", "balayage", "móc lai", "moc lai", "nâng tông", "nang tong",

                // === 5. TỪ KHÓA & THUẬT NGỮ TIẾNG ANH (DỰ PHÒNG KHÁCH KHÔNG GÕ TIẾNG VIỆT) ===
                "hair", "hairstyle", "haircut", "haircare", "barber", "salon", "stylist",
                "cut", "dye", "color", "perm", "fringe", "bangs", "trim", "shave", "bald",
                "straight", "curly", "wavy", "bleach", "bleaching", "blonde", "brunette",
                "ginger", "platinum", "silver", "grey", "afro", "cornrows", "ponytail", "braids"
        );

        // Quét xem câu lệnh có chứa từ khóa hợp lệ nào không
        return hairKeywords.stream().anyMatch(lowerPrompt::contains);
    }
}