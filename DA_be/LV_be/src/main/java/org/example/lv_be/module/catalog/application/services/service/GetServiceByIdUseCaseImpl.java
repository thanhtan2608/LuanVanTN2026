package org.example.lv_be.module.catalog.application.services.service;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.dto.service.ServiceResponse;
import org.example.lv_be.module.catalog.application.interfaces.service.IGetServiceByIdUseCase;
import org.example.lv_be.module.catalog.application.mappers.ServiceItemMapper;
import org.example.lv_be.module.catalog.domain.exception.CatalogDomainException;
import org.example.lv_be.module.catalog.domain.repository.IServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetServiceByIdUseCaseImpl implements IGetServiceByIdUseCase {

    private final IServiceRepository serviceRepository;
    private final ServiceItemMapper serviceMapper;

    @Override
    public ServiceResponse execute(Long id) {
        return serviceRepository.findById(id)
                .map(serviceMapper::toResponse)
                .orElseThrow(() -> new CatalogDomainException("Gói dịch vụ làm tóc này không tồn tại!"));
    }
}