package org.example.lv_be.module.banners.infrastructure.database;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.banners.domain.entity.Banner;
import org.example.lv_be.module.banners.domain.entity.BannerPosition;
import org.example.lv_be.module.banners.domain.repository.IBannerRepository;
import org.example.lv_be.module.banners.infrastructure.database.entity.BannerJpaEntity;
import org.example.lv_be.module.banners.infrastructure.database.mapper.BannerInfraMapper;
import org.example.lv_be.module.banners.infrastructure.database.repository.BannerSpringJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BannerRepositoryImpl implements IBannerRepository {

    private final BannerSpringJpaRepository jpaRepository;

    @Override
    public Banner save(Banner banner) {
        // Chuyển Domain thành JPA Entity -> Lưu -> Chuyển ngược lại thành Domain
        BannerJpaEntity jpaEntity = BannerInfraMapper.toJpaEntity(banner);
        BannerJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return BannerInfraMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Banner> findById(Long id) {
        return jpaRepository.findById(id)
                .map(BannerInfraMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Banner> findAll() {
        return jpaRepository.findAll().stream()
                .map(BannerInfraMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Banner> findActiveBannersByPosition(BannerPosition position) {
        return jpaRepository.findByPositionAndIsActiveTrueOrderByDisplayOrderAsc(position)
                .stream()
                .map(BannerInfraMapper::toDomain)
                .collect(Collectors.toList());
    }
}