package org.example.lv_be.module.branches.domain.repository;

import org.example.lv_be.module.branches.domain.entity.Branch;
import java.util.List;
import java.util.Optional;

public interface IBranchRepository {

    Optional<Branch> findById(Long id);

    List<Branch> findAllActive();

    List<Branch> findAllIncludingDeleted();

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Branch save(Branch branch);

    Optional<Branch> findByNameIncludingDeleted(String name);
}