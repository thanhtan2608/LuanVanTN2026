package org.example.lv_be.module.users.domain.repository;

import org.example.lv_be.common.enums.Role;
import org.example.lv_be.module.users.domain.entity.User;

import java.util.List;
import java.util.Optional;

// KHÔNG CÓ @Repository
// KHÔNG CÓ extends JpaRepository
public interface IUserRepository {

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    User save(User user); // Trả về User (Domain Entity), không phải JPA Entity
    Optional<User> findById(Long id);
    List<User> findByRoleIn(List<Role> roles);
    Optional<User> findByPhoneIncludingDeleted(String phone);
}