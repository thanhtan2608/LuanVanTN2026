package org.example.lv_be.module.ailookbook.application.interfaces;

import org.example.lv_be.module.ailookbook.application.dto.LookbookDetailResponse;

public interface IGetLookbookDetailUseCase {
    LookbookDetailResponse execute(Long id);
}