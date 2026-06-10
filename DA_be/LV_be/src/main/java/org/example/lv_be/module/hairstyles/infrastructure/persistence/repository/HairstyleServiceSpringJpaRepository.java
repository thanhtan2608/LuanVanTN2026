package org.example.lv_be.module.hairstyles.infrastructure.persistence.repository;

import org.example.lv_be.module.hairstyles.infrastructure.persistence.entity.HairstyleServiceId;
import org.example.lv_be.module.hairstyles.infrastructure.persistence.entity.HairstyleServiceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface HairstyleServiceSpringJpaRepository extends JpaRepository<HairstyleServiceJpaEntity, HairstyleServiceId> {

    List<HairstyleServiceJpaEntity> findByHairstyleId(Long hairstyleId);

    @Modifying
    @Query("DELETE FROM HairstyleServiceJpaEntity h WHERE h.hairstyleId = :hairstyleId")
    void deleteByHairstyleId(@Param("hairstyleId") Long hairstyleId);

    @Modifying
    @Query("DELETE FROM HairstyleServiceJpaEntity h WHERE h.hairstyleId = :hairstyleId AND h.serviceId = :serviceId")
    void deleteLink(@Param("hairstyleId") Long hairstyleId, @Param("serviceId") Long serviceId);

    boolean existsByHairstyleIdAndServiceId(Long hairstyleId, Long serviceId);
}