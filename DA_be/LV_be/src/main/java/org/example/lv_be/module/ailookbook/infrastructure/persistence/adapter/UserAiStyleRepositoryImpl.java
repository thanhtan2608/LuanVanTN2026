package org.example.lv_be.module.ailookbook.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.ailookbook.domain.entity.UserAiStyle;
import org.example.lv_be.module.ailookbook.domain.enums.AiProcessStatus;
import org.example.lv_be.module.ailookbook.domain.repository.IUserAiStyleRepository;
import org.example.lv_be.module.ailookbook.infrastructure.persistence.entity.UserAiStyleJpaEntity;
import org.example.lv_be.module.ailookbook.infrastructure.persistence.mapper.UserAiStylePersistenceMapper;
import org.example.lv_be.module.ailookbook.infrastructure.persistence.repository.UserAiStyleSpringJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAiStyleRepositoryImpl implements IUserAiStyleRepository {

    private final UserAiStyleSpringJpaRepository jpaRepository;
    private final UserAiStylePersistenceMapper mapper;

    @Override
    public Optional<UserAiStyle> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public List<UserAiStyle> findByUserId(Long userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<UserAiStyle> findByStatusIn(List<AiProcessStatus> statuses) {
        return jpaRepository.findByStatusIn(statuses).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public UserAiStyle save(UserAiStyle userAiStyle) {
        UserAiStyleJpaEntity jpa = mapper.toJpaEntity(userAiStyle);
        UserAiStyleJpaEntity saved = jpaRepository.save(jpa);
        return mapper.toDomainEntity(saved);
    }
}