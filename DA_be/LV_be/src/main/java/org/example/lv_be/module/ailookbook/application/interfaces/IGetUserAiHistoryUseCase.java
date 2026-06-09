package org.example.lv_be.module.ailookbook.application.interfaces;

import org.example.lv_be.module.ailookbook.application.dto.AiStyleResponse;
import java.util.List;

public interface IGetUserAiHistoryUseCase {
    List<AiStyleResponse> execute(Long userId);
}