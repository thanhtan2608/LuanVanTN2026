package org.example.lv_be.module.catalog.application.interfaces.service;

import org.example.lv_be.module.catalog.application.dto.service.ServiceResponse;

public interface IGetServiceByIdUseCase { ServiceResponse execute(Long id); }
