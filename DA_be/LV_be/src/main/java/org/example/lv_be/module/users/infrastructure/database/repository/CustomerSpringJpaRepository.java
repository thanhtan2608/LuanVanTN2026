package org.example.lv_be.module.users.infrastructure.database.repository;

import org.example.lv_be.module.users.infrastructure.database.entity.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSpringJpaRepository extends JpaRepository<CustomerJpaEntity, Long> {
}