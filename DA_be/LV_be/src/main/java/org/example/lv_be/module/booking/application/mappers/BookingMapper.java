package org.example.lv_be.module.booking.application.mappers;

import org.example.lv_be.module.booking.application.dto.BookingResponse;
import org.example.lv_be.module.booking.application.dto.CreateBookingRequest;
import org.example.lv_be.module.booking.domain.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Booking toDomain(CreateBookingRequest request);

    BookingResponse toResponse(Booking domain);
}