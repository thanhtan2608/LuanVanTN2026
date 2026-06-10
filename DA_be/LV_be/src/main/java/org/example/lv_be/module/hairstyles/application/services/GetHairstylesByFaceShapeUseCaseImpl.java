package org.example.lv_be.module.hairstyles.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.FaceShape;
import org.example.lv_be.module.hairstyles.application.dto.HairstyleResponse;
import org.example.lv_be.module.hairstyles.application.interfaces.IGetHairstylesByFaceShapeUseCase;
import org.example.lv_be.module.hairstyles.application.mapper.HairstyleMapper;
import org.example.lv_be.module.hairstyles.domain.repository.IHairstyleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetHairstylesByFaceShapeUseCaseImpl implements IGetHairstylesByFaceShapeUseCase {

    private final IHairstyleRepository hairstyleRepository;
    private final HairstyleMapper hairstyleMapper;

    @Override
    public List<HairstyleResponse> execute(FaceShape faceShape) {
        return hairstyleRepository.findByFaceShapeAndActiveTrue(faceShape).stream()
                .map(hairstyleMapper::toResponse)
                .toList();
    }
}