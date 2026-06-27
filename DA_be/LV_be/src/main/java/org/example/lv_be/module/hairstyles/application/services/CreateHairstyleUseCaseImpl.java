package org.example.lv_be.module.hairstyles.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.storage.ICloudStorageService;
import org.example.lv_be.module.hairstyles.application.dto.CreateHairstyleRequest;
import org.example.lv_be.module.hairstyles.application.dto.HairstyleResponse;
import org.example.lv_be.module.hairstyles.application.interfaces.ICreateHairstyleUseCase;
import org.example.lv_be.module.hairstyles.application.mapper.HairstyleMapper;
import org.example.lv_be.module.hairstyles.domain.entity.Hairstyle;
import org.example.lv_be.module.hairstyles.domain.exception.HairstyleDomainException;
import org.example.lv_be.module.hairstyles.domain.repository.IHairstyleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateHairstyleUseCaseImpl implements ICreateHairstyleUseCase {

    private final IHairstyleRepository hairstyleRepository;
    private final HairstyleMapper hairstyleMapper;
    private final ICloudStorageService cloudStorageService;

    @Override
    @Transactional
    public HairstyleResponse execute(CreateHairstyleRequest request, MultipartFile file) {
        if (hairstyleRepository.existsByName(request.getName())) {
            throw new HairstyleDomainException("Tên kiểu tóc này đã tồn tại trên hệ thống!");
        }

        Hairstyle hairstyle = hairstyleMapper.toDomain(request);

        if (file != null && !file.isEmpty()) {
            String url = cloudStorageService.uploadFile(file, "salon-hairstyles");
            hairstyle.setImageUrl(url);
        }

        hairstyle.setActive(true);
        hairstyle.setDeleted(false);
        hairstyle.setCreatedAt(LocalDateTime.now());

        hairstyle.validateSelf(); // Kích hoạt Rich Domain Validation

        return hairstyleMapper.toResponse(hairstyleRepository.sourceSave(hairstyle));
    }
}