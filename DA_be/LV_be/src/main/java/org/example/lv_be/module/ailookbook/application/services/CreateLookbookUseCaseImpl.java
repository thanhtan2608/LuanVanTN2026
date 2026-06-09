package org.example.lv_be.module.ailookbook.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.exception.AppException;
import org.example.lv_be.module.ailookbook.application.dto.CreateLookbookRequest;
import org.example.lv_be.module.ailookbook.application.dto.LookbookResponse;
import org.example.lv_be.module.ailookbook.application.interfaces.ICreateLookbookUseCase;
import org.example.lv_be.module.ailookbook.application.interfaces.external.ICloudStorageService;
import org.example.lv_be.module.ailookbook.application.mappers.LookbookMapper;
import org.example.lv_be.module.ailookbook.domain.entity.LookbookItem;
import org.example.lv_be.module.ailookbook.domain.enums.GenderCategory;
import org.example.lv_be.module.ailookbook.domain.repository.ILookbookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateLookbookUseCaseImpl implements ICreateLookbookUseCase {

    private final ILookbookRepository lookbookRepository;
    private final ICloudStorageService cloudStorageService;
    private final LookbookMapper lookbookMapper;

    @Override
    @Transactional
    public LookbookResponse execute(CreateLookbookRequest request, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "File hình ảnh mẫu thiết kế AI bắt buộc phải có!");
        }

        // Kiểm tra xem tiêu đề mẫu ảnh có trùng lặp bao gồm cả file xóa mềm không
        Optional<LookbookItem> duplicatedOpt = lookbookRepository.findByTitleIncludingDeleted(request.getTitle());
        LookbookItem targetItem;

        String uploadedUrl = cloudStorageService.uploadFile(imageFile, "lookbook-gallery");

        if (duplicatedOpt.isPresent()) {
            targetItem = duplicatedOpt.get();
            if (!targetItem.isDeleted()) {
                throw new AppException(HttpStatus.CONFLICT, "Tiêu đề bộ sưu tập mẫu tóc AI này đã tồn tại!");
            }
            // Khôi phục dữ liệu cũ bị xóa mềm
            targetItem.setDeleted(false);
            targetItem.setActive(true);
        } else {
            targetItem = lookbookMapper.toEntity(request);
            targetItem.setCreatedAt(LocalDateTime.now());
            targetItem.setActive(true);
            targetItem.setDeleted(false);
        }

        // Cập nhật đè dữ liệu mới
        targetItem.setTitle(request.getTitle());
        targetItem.setDescription(request.getDescription());
        targetItem.setImageUrl(uploadedUrl);
        targetItem.setGender(GenderCategory.valueOf(request.getGender().toUpperCase()));
        targetItem.setPrompt(request.getPrompt());
        targetItem.linkToActualHairstyle(request.getHairstyleId());

        LookbookItem savedItem = lookbookRepository.save(targetItem);
        return lookbookMapper.toResponse(savedItem);
    }
}