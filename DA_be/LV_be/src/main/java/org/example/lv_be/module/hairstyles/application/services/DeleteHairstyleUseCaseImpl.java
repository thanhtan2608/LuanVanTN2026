package org.example.lv_be.module.hairstyles.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.hairstyles.application.interfaces.IDeleteHairstyleUseCase;
import org.example.lv_be.module.hairstyles.domain.entity.Hairstyle;
import org.example.lv_be.module.hairstyles.domain.exception.HairstyleDomainException;
import org.example.lv_be.module.hairstyles.domain.repository.IHairstyleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteHairstyleUseCaseImpl implements IDeleteHairstyleUseCase {

    private final IHairstyleRepository hairstyleRepository;

    @Override
    @Transactional
    public void execute(Long id) {
        Hairstyle hairstyle = hairstyleRepository.findById(id)
                .orElseThrow(() -> new HairstyleDomainException("Không tìm thấy kiểu tóc yêu cầu xóa!"));

        hairstyle.softDelete(); // Đồng bộ trạng thái deleted = true ngầm bên dưới
        hairstyleRepository.sourceSave(hairstyle);
    }
}