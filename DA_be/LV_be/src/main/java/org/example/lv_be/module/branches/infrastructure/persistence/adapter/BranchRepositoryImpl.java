package org.example.lv_be.module.branches.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.branches.domain.entity.Branch;
import org.example.lv_be.module.branches.domain.repository.IBranchRepository;
import org.example.lv_be.module.branches.infrastructure.persistence.entity.BranchJpaEntity;
import org.example.lv_be.module.branches.infrastructure.persistence.mapper.BranchPersistenceMapper;
import org.example.lv_be.module.branches.infrastructure.persistence.repository.BranchSpringJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BranchRepositoryImpl implements IBranchRepository {

    private final BranchSpringJpaRepository jpaRepository;
    private final BranchPersistenceMapper persistenceMapper;

    @Override
    public Optional<Branch> findById(Long id) {
        return jpaRepository.findById(id)
                .map(persistenceMapper::toDomainEntity);
    }

    @Override
    public List<Branch> findAllActive() {
        return jpaRepository.findByIsActiveTrue().stream()
                .map(persistenceMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<Branch> findAllIncludingDeleted() {
        return jpaRepository.findByAllIncludingDeletedNative().stream()
                .map(persistenceMapper::toDomainEntity)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        return jpaRepository.existsByNameAndIdNot(name, id);
    }

    @Override
    public Optional<Branch> findByNameIncludingDeleted(String name) {
        return jpaRepository.findByNameIncludingDeleted(name)
                .map(persistenceMapper::toDomainEntity);
    }

    @Override
    public Branch save(Branch branch) {
        // 1. Chuyển đổi từ mô hình Domain sang mô hình JPA dính liền Database
        BranchJpaEntity jpaEntity = persistenceMapper.toJpaEntity(branch);

        // 2. Thực thi lưu xuống Database MySQL
        BranchJpaEntity savedEntity = jpaRepository.save(jpaEntity);

        // 3. Trả kết quả ngược lại bằng mô hình dữ liệu thuần của Domain
        return persistenceMapper.toDomainEntity(savedEntity);
    }
}