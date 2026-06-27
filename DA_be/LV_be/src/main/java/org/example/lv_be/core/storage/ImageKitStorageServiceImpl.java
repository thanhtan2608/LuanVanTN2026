package org.example.lv_be.core.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageKitStorageServiceImpl implements ICloudStorageService {

    private final RestTemplate aiRestTemplate; // Tái sử dụng RestTemplate đã cấu hình

    @Value("${app.imagekit.url-endpoint}")
    private String urlEndpoint;

    @Value("${app.imagekit.private-key}")
    private String privateKey;

    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống!");
        }

        try {
            log.info("Đang upload file lên ImageKit thật, thư mục: {}", folderName);

            // 1. Tạo Header mã hóa Private Key theo chuẩn Basic Auth của ImageKit
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            String auth = privateKey + ":"; // Định dạng mã hóa của ImageKit yêu cầu dấu hai chấm ở cuối
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            headers.set("Authorization", "Basic " + encodedAuth);

            // 2. Đóng gói dữ liệu Form-data
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new org.springframework.core.io.ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename(); // Rất quan trọng để ImageKit nhận diện định dạng (jpg, png)
                }
            }); // File nhị phân
            body.add("fileName", System.currentTimeMillis() + "_" + file.getOriginalFilename());
            body.add("folder", folderName);
            body.add("useUniqueFileName", "true");

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 3. Gửi lệnh POST sang API của ImageKit
            ResponseEntity<Map> response = aiRestTemplate.postForEntity(
                    "https://upload.imagekit.io/api/v1/files/upload",
                    requestEntity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("url")) {
                String uploadedUrl = responseBody.get("url").toString();
                log.info("Upload ImageKit thành công! URL thật: {}", uploadedUrl);
                return uploadedUrl;
            }

            throw new RuntimeException("Không nhận được URL phản hồi từ ImageKit!");

        } catch (Exception e) {
            log.error("Lỗi upload hình ảnh lên ImageKit: {}", e.getMessage());
            throw new RuntimeException("Hệ thống tải ảnh ImageKit gặp sự cố!");
        }
    }
}
