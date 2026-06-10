package org.example.lv_be.module.hairstyles.application.interfaces;

import org.example.lv_be.common.enums.FaceShape;
import org.example.lv_be.module.hairstyles.application.dto.HairstyleResponse;

import java.util.List;

public interface IGetHairstylesByFaceShapeUseCase { List<HairstyleResponse> execute(FaceShape faceShape); }
