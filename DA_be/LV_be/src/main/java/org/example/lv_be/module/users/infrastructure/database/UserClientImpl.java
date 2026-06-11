package org.example.lv_be.module.users.infrastructure.database;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.booking.application.interfaces.IUserClient;
import org.example.lv_be.module.users.infrastructure.database.entity.UserJpaEntity;
import org.example.lv_be.module.users.infrastructure.database.repository.UserSpringJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserClientImpl implements IUserClient {

    private final UserSpringJpaRepository userRepository; // Repository bên module Users của bạn

    @Override
    public Optional<Long> findUserIdByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .map(UserJpaEntity::getId);
    }
}
