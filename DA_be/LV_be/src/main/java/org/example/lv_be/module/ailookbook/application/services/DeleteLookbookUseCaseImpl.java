package org.example.lv_be.module.ailookbook.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.exception.AppException;
import org.example.lv_be.module.ailookbook.application.interfaces.IDeleteLookbookUseCase;
import org.example.lv_be.module.ailookbook.domain.entity.LookbookItem;
import org.example.lv_be.module.ailookbook.domain.repository.ILookbookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteLookbookUseCaseImpl implements IDeleteLookbookUseCase {
    private final ILookbookRepository lookbookRepository;

    @Override
    @Transactional
    public void execute(Long id) {
        LookbookItem item = lookbookRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Mẫu thiết kế cần xóa không tồn tại!"));
        item.setDeleted(true);
        item.deactivate();
        lookbookRepository.save(item);
    }
}