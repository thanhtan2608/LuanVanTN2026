package org.example.lv_be.module.banners.application.interfaces.in;

import org.example.lv_be.module.banners.application.dto.response.BannerResponse;
import org.example.lv_be.module.banners.domain.entity.BannerPosition;

import java.util.List;

public interface IGetBannerUseCase {
    List<BannerResponse> getAllBannersForAdmin();
    List<BannerResponse> getActiveBannersForDisplay(BannerPosition position);
}