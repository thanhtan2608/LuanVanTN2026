package org.example.lv_be.module.hairstyles.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.FaceShape;
import org.example.lv_be.module.hairstyles.domain.entity.Hairstyle;
import org.example.lv_be.module.hairstyles.domain.repository.IHairstyleRepository;
import org.example.lv_be.module.hairstyles.infrastructure.persistence.entity.HairstyleJpaEntity;
import org.example.lv_be.module.hairstyles.infrastructure.persistence.mapper.HairstylePersistenceMapper;
import org.example.lv_be.module.hairstyles.infrastructure.persistence.repository.HairstyleSpringJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HairstyleRepositoryImpl implements IHairstyleRepository {

    private final HairstyleSpringJpaRepository springJpaRepository;
    private final HairstylePersistenceMapper persistenceMapper;

    @Override
    public Optional<Hairstyle> findById(Long id) {
        return springJpaRepository.findById(id)
                .filter(jpa -> !jpa.isDeleted()) // Chặn đứng các bản ghi đã bị xóa mềm
                .map(persistenceMapper::toDomainEntity);
    }

    @Override
    public List<Hairstyle> findByFaceShapeAndActiveTrue(FaceShape faceShape) {
        return springJpaRepository.findByFaceShapeAndActiveTrueAndDeletedFalse(faceShape).stream()
                .map(persistenceMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<Hairstyle> findAllActive() {
        return springJpaRepository.findByActiveTrueAndDeletedFalse().stream()
                .map(persistenceMapper::toDomainEntity)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return springJpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        return springJpaRepository.existsByNameAndIdNot(name, id);
    }

    @Override
    public Hairstyle sourceSave(Hairstyle hairstyle) {
        HairstyleJpaEntity jpa = persistenceMapper.toJpaEntity(hairstyle);
        HairstyleJpaEntity saved = springJpaRepository.save(jpa);
        return persistenceMapper.toDomainEntity(saved);
    }
}