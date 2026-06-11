package org.example.lv_be.module.booking.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.booking.domain.entity.Booking;
import org.example.lv_be.module.booking.domain.repository.IBookingRepository;
import org.example.lv_be.module.booking.infrastructure.persistence.entity.BookingJpaEntity;
import org.example.lv_be.module.booking.infrastructure.persistence.mapper.BookingPersistenceMapper;
import org.example.lv_be.module.booking.infrastructure.persistence.repository.BookingSpringJpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements IBookingRepository {

    private final BookingSpringJpaRepository springJpaRepository;
    private final BookingPersistenceMapper mapper;

    @Override
    public Optional<Booking> findById(Long id) {
        return springJpaRepository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public Optional<Booking> findByCode(String code) {
        return springJpaRepository.findByCode(code).map(mapper::toDomainEntity);
    }
    @Override
    public List<Booking> findByCustomerId(Long customerId) {
        return springJpaRepository.findByCustomerIdOrderByBookingDateDesc(customerId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<Booking> findByStaffIdAndBookingDate(Long staffId, LocalDate bookingDate) {
        return springJpaRepository.findByStaffIdAndBookingDate(staffId, bookingDate).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<Booking> findByBranchIdAndBookingDate(Long branchId, LocalDate bookingDate) {
        return springJpaRepository.findByBranchIdAndBookingDate(branchId, bookingDate).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public Booking sourceSave(Booking booking) {
        BookingJpaEntity jpa = mapper.toJpaEntity(booking);
        BookingJpaEntity saved = springJpaRepository.save(jpa);
        return mapper.toDomainEntity(saved);
    }
}