package org.example.lv_be.module.users.domain.repository;

import org.example.lv_be.common.enums.Role;
import org.example.lv_be.module.users.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    List<User> findByRole(Role role);
    List<User> findActiveUsersByRole(Role role);
}