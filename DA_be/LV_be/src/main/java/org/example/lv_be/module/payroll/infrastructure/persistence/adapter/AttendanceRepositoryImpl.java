package org.example.lv_be.module.payroll.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.payroll.domain.entity.Attendance;
import org.example.lv_be.module.payroll.domain.enums.AttendanceStatus;
import org.example.lv_be.module.payroll.domain.repository.IAttendanceRepository;
import org.example.lv_be.module.payroll.infrastructure.persistence.entity.AttendanceJpaEntity;
import org.example.lv_be.module.payroll.infrastructure.persistence.mapper.PayrollPersistenceMapper;
import org.example.lv_be.module.payroll.infrastructure.persistence.repository.AttendanceSpringJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements IAttendanceRepository {
    private final AttendanceSpringJpaRepository jpaRepository;
    private final PayrollPersistenceMapper mapper;

    @Override
    public Optional<Attendance> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public Optional<Attendance> findByStaffIdAndWorkDate(Long staffId, LocalDate workDate) {
        return jpaRepository.findByStaffIdAndWorkDate(staffId, workDate).map(mapper::toDomainEntity);
    }

    @Override
    public Attendance sourceSave(Attendance attendance) {
        AttendanceJpaEntity jpaEntity = mapper.toJpaEntity(attendance);
        return mapper.toDomainEntity(jpaRepository.save(jpaEntity));
    }

    @Override
    public int countByStaffIdAndStatusAndWorkDateBetween(Long staffId, AttendanceStatus status, LocalDate startDate, LocalDate endDate) {
        return jpaRepository.countByStaffIdAndStatusAndWorkDateBetween(staffId, status, startDate, endDate);
    }
}