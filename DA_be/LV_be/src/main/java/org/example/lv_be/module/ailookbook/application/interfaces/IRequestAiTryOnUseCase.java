package org.example.lv_be.module.ailookbook.application.interfaces;

import org.example.lv_be.module.ailookbook.application.dto.AiStyleResponse;
import org.example.lv_be.module.ailookbook.application.dto.AiTryOnRequest;

public interface IRequestAiTryOnUseCase {
    /**
     * Tiếp nhận yêu cầu thử tóc từ khách hàng (Chỉ cần truyền UserId và Object Request)
     */
    AiStyleResponse execute(Long userId, AiTryOnRequest request);
}