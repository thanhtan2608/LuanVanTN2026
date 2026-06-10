package org.example.lv_be.module.hairstyles.application.interfaces;

import org.example.lv_be.module.hairstyles.application.dto.HairstyleDetailResponse;

public interface IGetHairstyleByIdUseCase { HairstyleDetailResponse execute(Long id); }
