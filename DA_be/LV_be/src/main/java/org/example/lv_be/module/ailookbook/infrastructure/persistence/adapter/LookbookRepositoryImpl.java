package org.example.lv_be.module.ailookbook.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.ailookbook.domain.entity.LookbookItem;
import org.example.lv_be.module.ailookbook.domain.repository.ILookbookRepository;
import org.example.lv_be.module.ailookbook.infrastructure.persistence.entity.LookbookJpaEntity;
import org.example.lv_be.module.ailookbook.infrastructure.persistence.mapper.LookbookPersistenceMapper;
import org.example.lv_be.module.ailookbook.infrastructure.persistence.repository.LookbookSpringJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LookbookRepositoryImpl implements ILookbookRepository {

    private final LookbookSpringJpaRepository jpaRepository;
    private final LookbookPersistenceMapper mapper;

    @Override
    public Optional<LookbookItem> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public List<LookbookItem> findAllActive() {
        return jpaRepository.findByActiveTrue().stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public Optional<LookbookItem> findByTitleIncludingDeleted(String title) {
        return jpaRepository.findByTitleIncludingDeleted(title).map(mapper::toDomainEntity);
    }

    @Override
    public boolean existsByTitle(String title) {
        return jpaRepository.existsByTitle(title);
    }

    @Override
    public boolean existsByTitleAndIdNot(String title, Long id) {
        return jpaRepository.existsByTitleAndIdNot(title, id);
    }

    @Override
    public LookbookItem save(LookbookItem item) {
        LookbookJpaEntity jpa = mapper.toJpaEntity(item);
        LookbookJpaEntity saved = jpaRepository.save(jpa);
        return mapper.toDomainEntity(saved);
    }
}