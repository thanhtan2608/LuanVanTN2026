package org.example.lv_be;

import org.example.lv_be.core.storage.ICloudStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
public class ImageKitTest {

    @Autowired
    private ICloudStorageService cloudStorageService;

    @Test
    public void testUploadImageKit() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "dummy image content".getBytes()
        );
        
        System.out.println(">>> Đang gọi ImageKit API...");
        String url = cloudStorageService.uploadFile(file, "test-folder");
        System.out.println("=========================================");
        System.out.println(">>> KẾT QUẢ UPLOAD THÀNH CÔNG!");
        System.out.println(">>> LINK ẢNH TRÊN IMAGEKIT: " + url);
        System.out.println("=========================================");
    }
}
