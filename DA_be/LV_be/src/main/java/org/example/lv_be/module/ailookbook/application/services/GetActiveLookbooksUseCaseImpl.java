package org.example.lv_be.module.ailookbook.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.ailookbook.application.dto.LookbookResponse;
import org.example.lv_be.module.ailookbook.application.interfaces.IGetActiveLookbooksUseCase;
import org.example.lv_be.module.ailookbook.application.mappers.LookbookMapper;
import org.example.lv_be.module.ailookbook.domain.entity.LookbookItem;
import org.example.lv_be.module.ailookbook.domain.repository.ILookbookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetActiveLookbooksUseCaseImpl implements IGetActiveLookbooksUseCase {
    private final ILookbookRepository lookbookRepository;
    private final LookbookMapper lookbookMapper;

    @Override
    @Transactional(readOnly = true)
    public List<LookbookResponse> execute(String genderFilter) {
        List<LookbookItem> items = lookbookRepository.findAllActive();

        if (genderFilter != null && !genderFilter.isBlank()) {
            return items.stream()
                    .filter(item -> item.getGender().name().equalsIgnoreCase(genderFilter))
                    .map(lookbookMapper::toResponse)
                    .toList();
        }

        return items.stream().map(lookbookMapper::toResponse).toList();
    }
}