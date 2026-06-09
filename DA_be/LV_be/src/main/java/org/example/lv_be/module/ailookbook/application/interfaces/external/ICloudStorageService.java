package org.example.lv_be.module.ailookbook.application.interfaces.external;

import org.springframework.web.multipart.MultipartFile;

public interface ICloudStorageService {
    /**
     * Tải tệp tin ảnh lên Cloud và trả về link URL trực tuyến tuyệt đối
     */
    String uploadFile(MultipartFile file, String folderName);
}