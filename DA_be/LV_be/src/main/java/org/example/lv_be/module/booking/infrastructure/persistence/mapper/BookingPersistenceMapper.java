package org.example.lv_be.module.booking.infrastructure.persistence.mapper;

import org.example.lv_be.module.booking.domain.entity.Booking;
import org.example.lv_be.module.booking.infrastructure.persistence.entity.BookingJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingPersistenceMapper {
    BookingJpaEntity toJpaEntity(Booking domain);
    Booking toDomainEntity(BookingJpaEntity jpa);
}