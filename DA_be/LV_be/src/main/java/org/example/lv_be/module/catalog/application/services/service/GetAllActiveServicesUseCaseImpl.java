package org.example.lv_be.module.catalog.application.services.service;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.dto.service.ServiceResponse;
import org.example.lv_be.module.catalog.application.interfaces.service.IGetAllActiveServicesUseCase;
import org.example.lv_be.module.catalog.application.mappers.ServiceItemMapper;
import org.example.lv_be.module.catalog.domain.repository.IServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAllActiveServicesUseCaseImpl implements IGetAllActiveServicesUseCase {

    private final IServiceRepository serviceRepository;
    private final ServiceItemMapper serviceMapper;

    @Override
    public List<ServiceResponse> execute() {
        return serviceRepository.findAllActive().stream()
                .map(serviceMapper::toResponse)
                .toList();
    }
}