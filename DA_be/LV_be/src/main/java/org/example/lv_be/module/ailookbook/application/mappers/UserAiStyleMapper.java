package org.example.lv_be.module.ailookbook.application.mappers;

import org.example.lv_be.module.ailookbook.application.dto.AiStyleResponse;
import org.example.lv_be.module.ailookbook.domain.entity.UserAiStyle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAiStyleMapper {

    /**
     * Chuyển đổi từ thực thể quản lý trạng thái AI (Domain) sang DTO phản hồi về cho Frontend
     * Giúp Frontend kiểm tra được trạng thái PENDING, PROCESSING, SUCCESS hoặc FAILED để hiển thị giao diện phù hợp
     */
    AiStyleResponse toResponse(UserAiStyle entity);
}