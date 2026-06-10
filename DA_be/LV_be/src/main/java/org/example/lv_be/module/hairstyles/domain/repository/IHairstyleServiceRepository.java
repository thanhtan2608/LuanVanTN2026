package org.example.lv_be.module.hairstyles.domain.repository;

import org.example.lv_be.module.hairstyles.domain.entity.HairstyleService;
import java.util.List;

public interface IHairstyleServiceRepository {

    // Tìm danh sách tất cả các mã ID dịch vụ kỹ thuật được gán cho 1 kiểu tóc cụ thể
    List<Long> findServiceIdsByHairstyleId(Long hairstyleId);

    // Lưu cặp liên kết mới (Thêm dịch vụ áp dụng cho kiểu tóc)
    void saveLink(HairstyleService hairstyleService);

    // Xóa liên kết (Bỏ dịch vụ ra khỏi kiểu tóc)
    void deleteLink(Long hairstyleId, Long serviceId);

    // Xóa toàn bộ liên kết dịch vụ cũ của một kiểu tóc (Phục vụ tính năng cập nhật/làm mới danh sách dịch vụ)
    void deleteAllLinksByHairstyleId(Long hairstyleId);

    boolean existsLink(Long hairstyleId, Long serviceId);
}