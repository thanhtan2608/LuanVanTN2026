package org.example.lv_be.module.catalog.application.interfaces.service;

import org.example.lv_be.module.catalog.application.dto.service.CreateServiceRequest;
import org.example.lv_be.module.catalog.application.dto.service.ServiceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ICreateServiceUseCase { ServiceResponse execute(CreateServiceRequest request, MultipartFile file); }
