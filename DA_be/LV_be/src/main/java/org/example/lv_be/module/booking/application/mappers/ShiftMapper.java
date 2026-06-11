package org.example.lv_be.module.booking.application.mappers;

import org.example.lv_be.module.booking.application.dto.shift.CreateShiftRequest;
import org.example.lv_be.module.booking.application.dto.shift.ShiftResponse;
import org.example.lv_be.module.booking.domain.entity.Shift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShiftMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Shift toDomain(CreateShiftRequest request);

    ShiftResponse toResponse(Shift domain);
}