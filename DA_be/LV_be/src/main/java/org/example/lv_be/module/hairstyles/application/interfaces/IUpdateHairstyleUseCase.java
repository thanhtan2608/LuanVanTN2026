package org.example.lv_be.module.hairstyles.application.interfaces;

import org.example.lv_be.module.hairstyles.application.dto.HairstyleResponse;
import org.example.lv_be.module.hairstyles.application.dto.UpdateHairstyleRequest;
import org.springframework.web.multipart.MultipartFile;

public interface IUpdateHairstyleUseCase { HairstyleResponse execute(Long id, UpdateHairstyleRequest request, MultipartFile file); }
