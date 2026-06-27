package org.example.lv_be.module.banners.application.interfaces.in;

import org.example.lv_be.module.banners.application.dto.request.CreateBannerRequest;
import org.example.lv_be.module.banners.application.dto.request.UpdateBannerRequest;
import org.example.lv_be.module.banners.application.dto.response.BannerResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IManageBannerUseCase {
    BannerResponse createBanner(CreateBannerRequest request, MultipartFile imageFile);
    BannerResponse updateBanner(Long id, UpdateBannerRequest request, MultipartFile imageFile);
    void deleteBanner(Long id);
    void toggleActiveStatus(Long id);
}