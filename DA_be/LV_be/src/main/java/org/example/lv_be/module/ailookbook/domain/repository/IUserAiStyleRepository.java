package org.example.lv_be.module.ailookbook.domain.repository;

import org.example.lv_be.module.ailookbook.domain.entity.UserAiStyle;
import org.example.lv_be.module.ailookbook.domain.enums.AiProcessStatus;
import java.util.List;
import java.util.Optional;

public interface IUserAiStyleRepository {

    Optional<UserAiStyle> findById(Long id);

    // Tìm kiếm toàn bộ lịch sử thử ảnh tóc AI của một khách hàng cụ thể
    List<UserAiStyle> findByUserId(Long userId);

    // Tìm các tác vụ đang treo (PENDING/PROCESSING) để hệ thống quét dọn hoặc chạy lại khi khởi động lại server
    List<UserAiStyle> findByStatusIn(List<AiProcessStatus> statuses);

    UserAiStyle save(UserAiStyle userAiStyle);
}