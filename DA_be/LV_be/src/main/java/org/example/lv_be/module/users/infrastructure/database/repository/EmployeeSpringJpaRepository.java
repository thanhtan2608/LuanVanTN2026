package org.example.lv_be.module.users.infrastructure.database.repository;

import org.example.lv_be.module.users.infrastructure.database.entity.EmployeeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeSpringJpaRepository extends JpaRepository<EmployeeJpaEntity, Long> {
    List<EmployeeJpaEntity> findByBranchId(Long branchId);
}