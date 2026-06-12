package org.example.lv_be.module.users.infrastructure.database.repository;
import org.example.lv_be.common.enums.Role;
import org.example.lv_be.module.users.infrastructure.database.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSpringJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByPhone(String phone);
    boolean existsByPhone(String phone);
    List<UserJpaEntity> findByRoleIn(List<Role> roles);
    @Query(value = "SELECT * FROM users WHERE phone = :phone", nativeQuery = true)
    Optional<UserJpaEntity> findByPhoneIncludingDeleted(@Param("phone") String phone);
    List<UserJpaEntity> findByRoleAndIsActiveTrue(Role role);
}