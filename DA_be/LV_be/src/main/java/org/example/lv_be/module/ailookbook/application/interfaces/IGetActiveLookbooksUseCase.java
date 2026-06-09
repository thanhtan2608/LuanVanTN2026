package org.example.lv_be.module.ailookbook.application.interfaces;

import org.example.lv_be.module.ailookbook.application.dto.LookbookResponse;
import java.util.List;

public interface IGetActiveLookbooksUseCase {
    List<LookbookResponse> execute(String genderFilter);
}