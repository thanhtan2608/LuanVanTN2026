package org.example.lv_be.module.ailookbook.infrastructure.persistence.repository;

import org.example.lv_be.module.ailookbook.infrastructure.persistence.entity.LookbookJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LookbookSpringJpaRepository extends JpaRepository<LookbookJpaEntity, Long> {

    List<LookbookJpaEntity> findByIsActiveTrue();

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    @Query(value = "SELECT * FROM lookbook_items WHERE title = :title", nativeQuery = true)
    Optional<LookbookJpaEntity> findByTitleIncludingDeleted(@Param("title") String title);
}