package org.example.lv_be.module.booking.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.booking.domain.entity.Shift;
import org.example.lv_be.module.booking.domain.repository.IShiftRepository;
import org.example.lv_be.module.booking.infrastructure.persistence.entity.ShiftJpaEntity;
import org.example.lv_be.module.booking.infrastructure.persistence.mapper.ShiftPersistenceMapper;
import org.example.lv_be.module.booking.infrastructure.persistence.repository.ShiftSpringJpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ShiftRepositoryImpl implements IShiftRepository {

    private final ShiftSpringJpaRepository springJpaRepository;
    private final ShiftPersistenceMapper mapper;

    @Override
    public Optional<Shift> findById(Long id) {
        return springJpaRepository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public Optional<Shift> findByStaffIdAndShiftDate(Long staffId, LocalDate shiftDate) {
        return springJpaRepository.findByStaffIdAndShiftDate(staffId, shiftDate)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Shift> findByShiftDate(LocalDate shiftDate) {
        return springJpaRepository.findByShiftDate(shiftDate).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public boolean existsByStaffIdAndShiftDate(Long staffId, LocalDate shiftDate) {
        return springJpaRepository.existsByStaffIdAndShiftDate(staffId, shiftDate);
    }

    @Override
    public Shift sourceSave(Shift shift) {
        ShiftJpaEntity jpa = mapper.toJpaEntity(shift);
        ShiftJpaEntity saved = springJpaRepository.save(jpa);
        return mapper.toDomainEntity(saved);
    }
}