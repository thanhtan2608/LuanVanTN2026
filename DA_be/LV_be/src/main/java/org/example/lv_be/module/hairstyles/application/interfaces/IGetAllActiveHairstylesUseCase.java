package org.example.lv_be.module.hairstyles.application.interfaces;

import org.example.lv_be.module.hairstyles.application.dto.HairstyleResponse;

import java.util.List;

public interface IGetAllActiveHairstylesUseCase { List<HairstyleResponse> execute(); }
