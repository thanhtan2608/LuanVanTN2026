package org.example.lv_be.module.catalog.application.services.service;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.interfaces.service.IDeleteServiceUseCase;
import org.example.lv_be.module.catalog.domain.entity.ServiceItem;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import org.example.lv_be.module.catalog.domain.repository.IServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteServiceUseCaseImpl implements IDeleteServiceUseCase {

    private final IServiceRepository serviceRepository;

    @Override
    @Transactional
    public void execute(Long id) {
        ServiceItem item = serviceRepository.findById(id)
                .orElseThrow(() -> new CatalogDomainException("Không tìm thấy dịch vụ yêu cầu xóa mềm!"));

        item.softDelete();
        serviceRepository.sourceSave(item);
    }
}