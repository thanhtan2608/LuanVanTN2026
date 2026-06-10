package org.example.lv_be.module.catalog.application.interfaces.service;

import org.example.lv_be.module.catalog.application.dto.service.ServiceResponse;
import org.example.lv_be.module.catalog.application.dto.service.UpdateServiceRequest;
import org.springframework.web.multipart.MultipartFile;

public interface IUpdateServiceUseCase { ServiceResponse execute(Long id, UpdateServiceRequest request, MultipartFile file); }
