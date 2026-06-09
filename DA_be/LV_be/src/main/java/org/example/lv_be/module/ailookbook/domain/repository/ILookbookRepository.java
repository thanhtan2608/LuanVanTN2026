package org.example.lv_be.module.ailookbook.domain.repository;

import org.example.lv_be.module.ailookbook.domain.entity.LookbookItem;
import java.util.List;
import java.util.Optional;

public interface ILookbookRepository {

    Optional<LookbookItem> findById(Long id);

    List<LookbookItem> findAllActive();

    // Quét xuyên thấu qua các bản ghi đã xóa mềm để phục vụ logic tái sử dụng/khôi phục dữ liệu
    Optional<LookbookItem> findByTitleIncludingDeleted(String title);

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    LookbookItem save(LookbookItem item);
}