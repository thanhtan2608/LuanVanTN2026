package org.example.lv_be.module.payroll.infrastructure.persistence.repository;

import org.example.lv_be.module.payroll.domain.enums.AttendanceStatus;
import org.example.lv_be.module.payroll.infrastructure.persistence.entity.AttendanceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceSpringJpaRepository extends JpaRepository<AttendanceJpaEntity, Long> {
    Optional<AttendanceJpaEntity> findByStaffIdAndWorkDate(Long staffId, LocalDate workDate);

    int countByStaffIdAndStatusAndWorkDateBetween(Long staffId, AttendanceStatus status, LocalDate startDate, LocalDate endDate);
}