package org.example.lv_be.module.users.application.mappers;

import org.example.lv_be.module.users.application.dto.RegisterRequest;
import org.example.lv_be.module.users.application.dto.UserProfileResponse;
import org.example.lv_be.module.users.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Chuyển từ Request -> Domain Entity
    // (Bỏ qua password vì mình sẽ tự băm mật khẩu thủ công ở Service)
    @Mapping(target = "password", ignore = true)
    User toDomainEntity(RegisterRequest request);

    // Chuyển từ Domain Entity -> Response để trả về cho Frontend
    UserProfileResponse toProfileResponse(User user);
}