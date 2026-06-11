package org.example.lv_be.module.booking.infrastructure.persistence.mapper;

import org.example.lv_be.module.booking.domain.entity.BookingService;
import org.example.lv_be.module.booking.infrastructure.persistence.entity.BookingServiceJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingServicePersistenceMapper {
    BookingServiceJpaEntity toJpaEntity(BookingService domain);
    BookingService toDomainEntity(BookingServiceJpaEntity jpa);
}