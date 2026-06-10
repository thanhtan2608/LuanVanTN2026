package org.example.lv_be.module.hairstyles.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.hairstyles.domain.entity.HairstyleService;
import org.example.lv_be.module.hairstyles.domain.repository.IHairstyleServiceRepository;
import org.example.lv_be.module.hairstyles.infrastructure.persistence.entity.HairstyleServiceJpaEntity;
import org.example.lv_be.module.hairstyles.infrastructure.persistence.mapper.HairstyleServicePersistenceMapper;
import org.example.lv_be.module.hairstyles.infrastructure.persistence.repository.HairstyleServiceSpringJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HairstyleServiceRepositoryImpl implements IHairstyleServiceRepository {

    private final HairstyleServiceSpringJpaRepository springJpaRepository;
    private final HairstyleServicePersistenceMapper persistenceMapper;

    @Override
    public List<Long> findServiceIdsByHairstyleId(Long hairstyleId) {
        return springJpaRepository.findByHairstyleId(hairstyleId).stream()
                .map(HairstyleServiceJpaEntity::getServiceId)
                .toList();
    }

    @Override
    public void saveLink(HairstyleService hairstyleService) {
        HairstyleServiceJpaEntity jpa = persistenceMapper.toJpaEntity(hairstyleService);
        springJpaRepository.save(jpa);
    }

    @Override
    public void deleteLink(Long hairstyleId, Long serviceId) {
        springJpaRepository.deleteLink(hairstyleId, serviceId);
    }

    @Override
    public void deleteAllLinksByHairstyleId(Long hairstyleId) {
        springJpaRepository.deleteByHairstyleId(hairstyleId);
    }

    @Override
    public boolean existsLink(Long hairstyleId, Long serviceId) {
        return springJpaRepository.existsByHairstyleIdAndServiceId(hairstyleId, serviceId);
    }
}