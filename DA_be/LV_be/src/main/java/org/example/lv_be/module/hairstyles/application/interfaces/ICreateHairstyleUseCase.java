package org.example.lv_be.module.hairstyles.application.interfaces;

import org.example.lv_be.module.hairstyles.application.dto.CreateHairstyleRequest;
import org.example.lv_be.module.hairstyles.application.dto.HairstyleResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ICreateHairstyleUseCase { HairstyleResponse execute(CreateHairstyleRequest request, MultipartFile file); }
