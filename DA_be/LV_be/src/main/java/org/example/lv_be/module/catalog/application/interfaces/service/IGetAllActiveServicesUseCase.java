package org.example.lv_be.module.catalog.application.interfaces.service;

import org.example.lv_be.module.catalog.application.dto.service.ServiceResponse;

import java.util.List;

public interface IGetAllActiveServicesUseCase { List<ServiceResponse> execute(); }
