package org.example.lv_be.module.ailookbook.application.interfaces;

import org.example.lv_be.module.ailookbook.application.dto.CreateLookbookRequest;
import org.example.lv_be.module.ailookbook.application.dto.LookbookResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ICreateLookbookUseCase {
    LookbookResponse execute(CreateLookbookRequest request, MultipartFile imageFile);
}