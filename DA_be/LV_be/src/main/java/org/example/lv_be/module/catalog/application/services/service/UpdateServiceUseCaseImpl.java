package org.example.lv_be.module.catalog.application.services.service;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.ailookbook.application.interfaces.external.ICloudStorageService;
import org.example.lv_be.module.catalog.application.dto.service.UpdateServiceRequest;
import org.example.lv_be.module.catalog.application.dto.service.ServiceResponse;
import org.example.lv_be.module.catalog.application.interfaces.service.IUpdateServiceUseCase;
import org.example.lv_be.module.catalog.application.mappers.ServiceItemMapper;
import org.example.lv_be.module.catalog.domain.entity.ServiceItem;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import org.example.lv_be.module.catalog.domain.repository.ICategoryRepository;
import org.example.lv_be.module.catalog.domain.repository.IServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UpdateServiceUseCaseImpl implements IUpdateServiceUseCase {

    private final IServiceRepository serviceRepository;
    private final ICategoryRepository categoryRepository;
    private final ServiceItemMapper serviceMapper;
    private final ICloudStorageService cloudStorageService;

    @Override
    @Transactional
    public ServiceResponse execute(Long id, UpdateServiceRequest request, MultipartFile file) {
        ServiceItem item = serviceRepository.findById(id)
                .orElseThrow(() -> new CatalogDomainException("Không tìm thấy thông tin gói dịch vụ cần sửa!"));

        categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CatalogDomainException("Danh mục mới chuyển sang không hợp lệ!"));

        item.setCategoryId(request.getCategoryId());
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDurationMinutes(request.getDurationMinutes());
        item.setActive(request.isActive());

        if (file != null && !file.isEmpty()) {
            String uploadedUrl = cloudStorageService.uploadFile(file, "salon-services");
            item.setImageUrl(uploadedUrl);
        }

        item.validateSelf();
        ServiceItem updatedItem = serviceRepository.sourceSave(item);
        return serviceMapper.toResponse(updatedItem);
    }
}