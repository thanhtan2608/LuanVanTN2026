package org.example.lv_be.module.booking.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.booking.domain.entity.BookingService;
import org.example.lv_be.module.booking.domain.repository.IBookingServiceRepository;
import org.example.lv_be.module.booking.infrastructure.persistence.entity.BookingServiceJpaEntity;
import org.example.lv_be.module.booking.infrastructure.persistence.mapper.BookingServicePersistenceMapper;
import org.example.lv_be.module.booking.infrastructure.persistence.repository.BookingServiceSpringJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookingServiceRepositoryImpl implements IBookingServiceRepository {

    private final BookingServiceSpringJpaRepository springJpaRepository;
    private final BookingServicePersistenceMapper mapper;

    @Override
    public List<BookingService> findByBookingId(Long bookingId) {
        return springJpaRepository.findByBookingId(bookingId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public void sourceSaveAll(List<BookingService> bookingServices) {
        List<BookingServiceJpaEntity> jpaList = bookingServices.stream()
                .map(mapper::toJpaEntity)
                .toList();
        springJpaRepository.saveAll(jpaList);
    }
    @Override
    public void deleteLink(Long bookingId, Long serviceId) {
        springJpaRepository.deleteByBookingIdAndServiceId(bookingId, serviceId);
    }
}