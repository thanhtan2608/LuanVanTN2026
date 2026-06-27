package org.example.lv_be.module.ailookbook.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.exception.AppException;
import org.example.lv_be.module.ailookbook.application.dto.LookbookResponse;
import org.example.lv_be.module.ailookbook.application.dto.UpdateLookbookRequest;
import org.example.lv_be.module.ailookbook.application.interfaces.IUpdateLookbookUseCase;
import org.example.lv_be.core.storage.ICloudStorageService;
import org.example.lv_be.module.ailookbook.application.mappers.LookbookMapper;
import org.example.lv_be.module.ailookbook.domain.entity.LookbookItem;
import org.example.lv_be.module.ailookbook.domain.enums.GenderCategory;
import org.example.lv_be.module.ailookbook.domain.repository.ILookbookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UpdateLookbookUseCaseImpl implements IUpdateLookbookUseCase {

    private final ILookbookRepository lookbookRepository;
    private final ICloudStorageService cloudStorageService;
    private final LookbookMapper lookbookMapper;

    @Override
    @Transactional
    public LookbookResponse execute(Long id, UpdateLookbookRequest request, MultipartFile imageFile) {
        LookbookItem item = lookbookRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy mẫu tóc AI chỉ định!"));

        if (lookbookRepository.existsByTitleAndIdNot(request.getTitle(), id)) {
            throw new AppException(HttpStatus.CONFLICT, "Tiêu đề cấu hình mới đã bị trùng lặp!");
        }

        lookbookMapper.updateEntityFromDto(request, item);
        item.setGender(GenderCategory.valueOf(request.getGender().toUpperCase()));
        item.linkToActualHairstyle(request.getHairstyleId());

        if (imageFile != null && !imageFile.isEmpty()) {
            String newUrl = cloudStorageService.uploadFile(imageFile, "lookbook-gallery");
            item.setImageUrl(newUrl);
        }

        LookbookItem updated = lookbookRepository.save(item);
        return lookbookMapper.toResponse(updated);
    }
}