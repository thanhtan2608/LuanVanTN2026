// HairstyleMapper.java
package org.example.lv_be.module.hairstyles.application.mapper;

import org.example.lv_be.module.hairstyles.application.dto.HairstyleResponse;
import org.example.lv_be.module.hairstyles.application.dto.HairstyleDetailResponse;
import org.example.lv_be.module.hairstyles.application.dto.CreateHairstyleRequest;
import org.example.lv_be.module.hairstyles.domain.entity.Hairstyle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HairstyleMapper {
    Hairstyle toDomain(CreateHairstyleRequest request);

    HairstyleResponse toResponse(Hairstyle domain);

    @Mapping(target = "actualServices", ignore = true) // Sẽ được map thủ công bằng logic liên module ở ServiceImpl
    HairstyleDetailResponse toDetailResponse(Hairstyle domain);
}