package org.example.lv_be.module.ailookbook.application.interfaces;

import org.example.lv_be.module.ailookbook.application.dto.LookbookResponse;
import org.example.lv_be.module.ailookbook.application.dto.UpdateLookbookRequest;
import org.springframework.web.multipart.MultipartFile;

public interface IUpdateLookbookUseCase {
    LookbookResponse execute(Long id, UpdateLookbookRequest request, MultipartFile imageFile);
}