package org.example.lv_be.module.hairstyles.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.storage.ICloudStorageService;
import org.example.lv_be.module.hairstyles.application.dto.UpdateHairstyleRequest;
import org.example.lv_be.module.hairstyles.application.dto.HairstyleResponse;
import org.example.lv_be.module.hairstyles.application.interfaces.IUpdateHairstyleUseCase;
import org.example.lv_be.module.hairstyles.application.mapper.HairstyleMapper;
import org.example.lv_be.module.hairstyles.domain.entity.Hairstyle;
import org.example.lv_be.module.hairstyles.domain.exception.HairstyleDomainException;
import org.example.lv_be.module.hairstyles.domain.repository.IHairstyleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UpdateHairstyleUseCaseImpl implements IUpdateHairstyleUseCase {

    private final IHairstyleRepository hairstyleRepository;
    private final HairstyleMapper hairstyleMapper;
    private final ICloudStorageService cloudStorageService;

    @Override
    @Transactional
    public HairstyleResponse execute(Long id, UpdateHairstyleRequest request, MultipartFile file) {
        Hairstyle hairstyle = hairstyleRepository.findById(id)
                .orElseThrow(() -> new HairstyleDomainException("Không tìm thấy kiểu tóc yêu cầu chỉnh sửa!"));

        if (hairstyleRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new HairstyleDomainException("Tên kiểu tóc mới chỉnh sửa bị trùng với kiểu tóc khác!");
        }

        hairstyle.setName(request.getName());
        hairstyle.setFaceShape(request.getFaceShape());
        hairstyle.setDescription(request.getDescription());
        hairstyle.setActive(request.isActive());

        if (file != null && !file.isEmpty()) {
            String url = cloudStorageService.uploadFile(file, "salon-hairstyles");
            hairstyle.setImageUrl(url);
        }

        hairstyle.validateSelf();
        return hairstyleMapper.toResponse(hairstyleRepository.sourceSave(hairstyle));
    }
}