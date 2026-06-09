package org.example.lv_be.module.ailookbook.application.mappers;

import org.example.lv_be.module.ailookbook.application.dto.CreateLookbookRequest;
import org.example.lv_be.module.ailookbook.application.dto.LookbookDetailResponse;
import org.example.lv_be.module.ailookbook.application.dto.LookbookResponse;
import org.example.lv_be.module.ailookbook.application.dto.UpdateLookbookRequest;
import org.example.lv_be.module.ailookbook.domain.entity.LookbookItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LookbookMapper {

    /**
     * Chuyển từ Request tạo mới sang Domain Entity
     * Bỏ qua các trường hệ thống tự sinh hoặc xử lý bằng file (imageUrl) để tránh ghi đè dữ liệu rác
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LookbookItem toEntity(CreateLookbookRequest request);

    /**
     * Chuyển từ Domain Entity sang Response hiển thị ngoài bộ sưu tập (Gallery)
     */
    LookbookResponse toResponse(LookbookItem item);

    /**
     * Chuyển từ Domain Entity sang Response chi tiết mẫu tóc
     * Trường 'actualServices' được ignore vì chúng ta sẽ dùng JdbcTemplate để map thủ công từ module Dịch vụ vào
     */
    @Mapping(target = "actualServices", ignore = true)
    LookbookDetailResponse toDetailResponse(LookbookItem item);

    /**
     * Cập nhật đè dữ liệu từ Request chỉnh sửa vào thực thể Domain hiện tại
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(UpdateLookbookRequest dto, @MappingTarget LookbookItem item);
}