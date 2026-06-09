package org.example.lv_be.module.ailookbook.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.ailookbook.application.dto.AiStyleResponse;
import org.example.lv_be.module.ailookbook.application.interfaces.IGetUserAiHistoryUseCase;
import org.example.lv_be.module.ailookbook.application.mappers.UserAiStyleMapper;
import org.example.lv_be.module.ailookbook.domain.entity.UserAiStyle;
import org.example.lv_be.module.ailookbook.domain.repository.IUserAiStyleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUserAiHistoryUseCaseImpl implements IGetUserAiHistoryUseCase {
    private final IUserAiStyleRepository userAiStyleRepository;
    private final UserAiStyleMapper aiStyleMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AiStyleResponse> execute(Long userId) {
        List<UserAiStyle> history = userAiStyleRepository.findByUserId(userId);
        return history.stream()
                .map(aiStyleMapper::toResponse)
                .toList();
    }
}