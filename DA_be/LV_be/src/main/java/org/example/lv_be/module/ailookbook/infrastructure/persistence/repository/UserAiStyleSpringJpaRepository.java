package org.example.lv_be.module.ailookbook.infrastructure.persistence.repository;

import org.example.lv_be.module.ailookbook.domain.enums.AiProcessStatus;
import org.example.lv_be.module.ailookbook.infrastructure.persistence.entity.UserAiStyleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAiStyleSpringJpaRepository extends JpaRepository<UserAiStyleJpaEntity, Long> {

    List<UserAiStyleJpaEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<UserAiStyleJpaEntity> findByStatusIn(List<AiProcessStatus> statuses);
}