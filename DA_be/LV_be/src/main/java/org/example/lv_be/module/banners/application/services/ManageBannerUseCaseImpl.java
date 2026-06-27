package org.example.lv_be.module.banners.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lv_be.core.storage.ICloudStorageService;
import org.example.lv_be.module.banners.application.dto.request.CreateBannerRequest;
import org.example.lv_be.module.banners.application.dto.request.UpdateBannerRequest;
import org.example.lv_be.module.banners.application.dto.response.BannerResponse;
import org.example.lv_be.module.banners.application.interfaces.in.IManageBannerUseCase;
import org.example.lv_be.module.banners.application.mappers.BannerApplicationMapper;
import org.example.lv_be.module.banners.domain.entity.Banner;
import org.example.lv_be.module.banners.domain.exception.BannerNotFoundException;
import org.example.lv_be.module.banners.domain.repository.IBannerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManageBannerUseCaseImpl implements IManageBannerUseCase {

    private final IBannerRepository bannerRepository;
    private final ICloudStorageService cloudStorageService; // Tái sử dụng service ImageKit

    @Override
    @Transactional
    public BannerResponse createBanner(CreateBannerRequest request, MultipartFile imageFile) {
        log.info("Bắt đầu tạo Banner mới: {}", request.getTitle());

        // 1. Upload ảnh lên ImageKit
        String uploadedImageUrl = cloudStorageService.uploadFile(imageFile, "banners");

        // 2. Build Domain Entity
        Banner newBanner = Banner.builder()
                .title(request.getTitle())
                .targetUrl(request.getTargetUrl())
                .position(request.getPosition())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .imageUrl(uploadedImageUrl) // Gắn link ảnh vào DB
                .isActive(true)
                .build();

        // 3. Lưu xuống Database
        Banner savedBanner = bannerRepository.save(newBanner);
        return BannerApplicationMapper.toResponse(savedBanner);
    }

    @Override
    @Transactional
    public BannerResponse updateBanner(Long id, UpdateBannerRequest request, MultipartFile imageFile) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new BannerNotFoundException(id));

        // 1. Kiểm tra nếu có up ảnh mới thì thay thế, không thì giữ link cũ
        if (imageFile != null && !imageFile.isEmpty()) {
            String newImageUrl = cloudStorageService.uploadFile(imageFile, "banners");
            banner.setImageUrl(newImageUrl);
        }

        // 2. Cập nhật các trường text (nếu có truyền)
        if (request.getTitle() != null) banner.setTitle(request.getTitle());
        if (request.getTargetUrl() != null) banner.setTargetUrl(request.getTargetUrl());
        if (request.getPosition() != null) banner.setPosition(request.getPosition());
        if (request.getDisplayOrder() != null) banner.setDisplayOrder(request.getDisplayOrder());

        // 3. Lưu lại
        Banner updatedBanner = bannerRepository.save(banner);
        return BannerApplicationMapper.toResponse(updatedBanner);
    }

    @Override
    @Transactional
    public void deleteBanner(Long id) {
        bannerRepository.findById(id).orElseThrow(() -> new BannerNotFoundException(id));
        bannerRepository.deleteById(id); // Xóa cứng (Hard Delete)
    }

    @Override
    @Transactional
    public void toggleActiveStatus(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new BannerNotFoundException(id));

        banner.toggleActive(); // Gọi logic của Entity
        bannerRepository.save(banner);
    }
}