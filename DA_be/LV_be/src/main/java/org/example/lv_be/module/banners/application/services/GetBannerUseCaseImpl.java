package org.example.lv_be.module.banners.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.banners.application.dto.response.BannerResponse;
import org.example.lv_be.module.banners.application.interfaces.in.IGetBannerUseCase;
import org.example.lv_be.module.banners.application.mappers.BannerApplicationMapper;
import org.example.lv_be.module.banners.domain.entity.BannerPosition;
import org.example.lv_be.module.banners.domain.repository.IBannerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetBannerUseCaseImpl implements IGetBannerUseCase {

    private final IBannerRepository bannerRepository;

    @Override
    public List<BannerResponse> getAllBannersForAdmin() {
        return bannerRepository.findAll().stream()
                .map(BannerApplicationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BannerResponse> getActiveBannersForDisplay(BannerPosition position) {
        // Lấy danh sách banner ĐANG BẬT và sắp xếp tăng dần theo DisplayOrder
        return bannerRepository.findActiveBannersByPosition(position).stream()
                .map(BannerApplicationMapper::toResponse)
                .collect(Collectors.toList());
    }
}