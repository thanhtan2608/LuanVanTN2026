package org.example.lv_be.module.banners.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.banners.application.dto.response.BannerResponse;
import org.example.lv_be.module.banners.application.interfaces.in.IGetBannerUseCase;
import org.example.lv_be.module.banners.domain.entity.BannerPosition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class PublicBannerController {

    private final IGetBannerUseCase getBannerUseCase;

    /**
     * =========================================================================
     * HƯỚNG DẪN CHO FRONTEND (CALL API LẤY BANNER HIỂN THỊ LÊN WEBSITE)
     * =========================================================================
     * Method: GET
     * URL: /api/v1/banners
     * Headers: Không cần gửi Token (Public API)
     * Content-Type: application/json
     * * Query Parameter bắt buộc:
     * - position: Vị trí cần lấy banner. Phải truyền đúng tên Enum ở Tầng Domain.
     * Các vị trí hợp lệ: HOME_TOP_SLIDER, HOME_MIDDLE_ADS, PRODUCT_SIDEBAR, PROMO_POPUP
     * * Ví dụ đường dẫn gọi thực tế từ Frontend:
     * GET /api/v1/banners?position=HOME_TOP_SLIDER
     * * Logic Backend tự xử lý:
     * - Chỉ trả ra các banner có trạng thái hoạt động (is_active = 1).
     * - Đã tự động sắp xếp theo thứ tự ưu tiên tăng dần (display_order từ nhỏ đến lớn).
     * =========================================================================
     */
    @GetMapping
    public ResponseEntity<List<BannerResponse>> getActiveBanners(
            @RequestParam("position") BannerPosition position) {
        return ResponseEntity.ok(getBannerUseCase.getActiveBannersForDisplay(position));
    }
}