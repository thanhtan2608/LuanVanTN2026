package org.example.lv_be.module.catalog.application.services.service;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.catalog.application.dto.service.ServiceResponse;
import org.example.lv_be.module.catalog.application.interfaces.service.IGetServicesByCategoryUseCase;
import org.example.lv_be.module.catalog.application.mappers.ServiceItemMapper;
import org.example.lv_be.module.catalog.domain.repository.IServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetServicesByCategoryUseCaseImpl implements IGetServicesByCategoryUseCase {

    private final IServiceRepository serviceRepository;
    private final ServiceItemMapper serviceMapper;

    @Override
    public List<ServiceResponse> execute(Long categoryId) {
        return serviceRepository.findByCategoryIdAndActiveTrue(categoryId).stream()
                .map(serviceMapper::toResponse)
                .toList();
    }
}