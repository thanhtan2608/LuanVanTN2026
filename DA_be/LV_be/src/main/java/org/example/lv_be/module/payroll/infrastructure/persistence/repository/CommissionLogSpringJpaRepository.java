package org.example.lv_be.module.payroll.infrastructure.persistence.repository;

import org.example.lv_be.module.payroll.infrastructure.persistence.entity.CommissionLogJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface CommissionLogSpringJpaRepository extends JpaRepository<CommissionLogJpaEntity, Long> {

    @Query("SELECT SUM(c.amount) FROM CommissionLogJpaEntity c WHERE c.staffId = :staffId AND c.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByStaffIdAndDateRange(@Param("staffId") Long staffId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}