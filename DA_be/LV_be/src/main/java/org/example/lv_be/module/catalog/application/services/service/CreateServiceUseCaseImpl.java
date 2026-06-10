package org.example.lv_be.module.catalog.application.services.service;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.ailookbook.application.interfaces.external.ICloudStorageService;
import org.example.lv_be.module.catalog.application.dto.service.CreateServiceRequest;
import org.example.lv_be.module.catalog.application.dto.service.ServiceResponse;
import org.example.lv_be.module.catalog.application.interfaces.service.ICreateServiceUseCase;
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
public class CreateServiceUseCaseImpl implements ICreateServiceUseCase {

    private final IServiceRepository serviceRepository;
    private final ICategoryRepository categoryRepository;
    private final ServiceItemMapper serviceMapper;
    private final ICloudStorageService cloudStorageService; // Tái sử dụng lõi kho ảnh ImageKit

    @Override
    @Transactional
    public ServiceResponse execute(CreateServiceRequest request, MultipartFile file) {
        categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CatalogDomainException("Danh mục dịch vụ đính kèm không hợp lệ!"));

        if (serviceRepository.existsByName(request.getName())) {
            throw new CatalogDomainException("Tên gói dịch vụ kỹ thuật làm tóc này đã tồn tại!");
        }

        ServiceItem item = serviceMapper.toDomain(request);

        // Upload ảnh menu bảng giá lên ImageKit nếu có
        if (file != null && !file.isEmpty()) {
            String uploadedUrl = cloudStorageService.uploadFile(file, "salon-services");
            item.setImageUrl(uploadedUrl);
        }

        item.setActive(true);
        item.setDeleted(false);
        item.validateSelf();

        ServiceItem savedItem = serviceRepository.sourceSave(item);
        return serviceMapper.toResponse(savedItem);
    }
}