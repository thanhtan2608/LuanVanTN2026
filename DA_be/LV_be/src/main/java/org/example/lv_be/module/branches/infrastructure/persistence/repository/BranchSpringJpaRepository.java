package org.example.lv_be.module.branches.infrastructure.persistence.repository;

import org.example.lv_be.module.branches.infrastructure.persistence.entity.BranchJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BranchSpringJpaRepository extends JpaRepository<BranchJpaEntity, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<BranchJpaEntity> findByIsActiveTrue();

    // Phá vỡ @SQLRestriction bằng Native Query để phục vụ logic phục hồi dữ liệu cũ
    @Query(value = "SELECT * FROM branches WHERE name = :name", nativeQuery = true)
    Optional<BranchJpaEntity> findByNameIncludingDeleted(@Param("name") String name);

    // Lấy toàn bộ dữ liệu trong bảng kể cả các bản ghi đã xóa mềm
    @Query(value = "SELECT * FROM branches", nativeQuery = true)
    List<BranchJpaEntity> findByAllIncludingDeletedNative();
}