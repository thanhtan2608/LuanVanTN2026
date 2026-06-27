package org.example.lv_be.module.banners.domain.repository;

import org.example.lv_be.module.banners.domain.entity.Banner;
import org.example.lv_be.module.banners.domain.entity.BannerPosition;

import java.util.List;
import java.util.Optional;

public interface IBannerRepository {

    // Lưu mới hoặc Cập nhật banner
    Banner save(Banner banner);

    // Tìm banner theo ID
    Optional<Banner> findById(Long id);

    // Xóa vật lý (Hard delete) một banner
    void deleteById(Long id);

    // Lấy TẤT CẢ banner (Dành cho Admin quản trị)
    List<Banner> findAll();

    // Lấy các banner ĐANG BẬT theo vị trí (Dành cho Khách hàng xem trang chủ)
    // Cần đảm bảo thứ tự tăng dần theo displayOrder
    List<Banner> findActiveBannersByPosition(BannerPosition position);
}