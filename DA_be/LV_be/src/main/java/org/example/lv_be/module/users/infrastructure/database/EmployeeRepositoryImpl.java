package org.example.lv_be.module.users.infrastructure.database;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.users.domain.entity.Employee;
import org.example.lv_be.module.users.domain.repository.IEmployeeRepository;
import org.example.lv_be.module.users.infrastructure.database.entity.EmployeeJpaEntity;
import org.example.lv_be.module.users.infrastructure.database.repository.EmployeeSpringJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements IEmployeeRepository {

    private final EmployeeSpringJpaRepository springRepository;

    @Override
    public Employee save(Employee employee) {
        EmployeeJpaEntity entity = EmployeeJpaEntity.builder()
                .userId(employee.getUserId())
                .branchId(employee.getBranchId())
                .baseSalary(employee.getBaseSalary())
                .commissionRate(employee.getCommissionRate())
                .build();

        EmployeeJpaEntity savedEntity = springRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Employee> findByUserId(Long userId) {
        return springRepository.findById(userId).map(this::toDomain);
    }

    @Override
    public List<Employee> findByBranchId(Long branchId) {
        return springRepository.findByBranchId(branchId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Employee toDomain(EmployeeJpaEntity entity) {
        return Employee.builder()
                .userId(entity.getUserId())
                .branchId(entity.getBranchId())
                .baseSalary(entity.getBaseSalary())
                .commissionRate(entity.getCommissionRate())
                .build();
    }
}