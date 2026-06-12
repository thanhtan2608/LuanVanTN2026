package org.example.lv_be.module.users.infrastructure.database;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.Role;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.example.lv_be.module.users.infrastructure.database.entity.UserJpaEntity;
import org.example.lv_be.module.users.infrastructure.database.repository.UserSpringJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements IUserRepository {

    private final UserSpringJpaRepository jpaRepository;

    @Override
    public Optional<User> findByPhone(String phone) {
        return jpaRepository.findByPhone(phone).map(this::toDomain);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return jpaRepository.existsByPhone(phone);
    }

    @Override
    public User save(User user) {
        UserJpaEntity saved = jpaRepository.save(toJpa(user));
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<User> findByRoleIn(List<Role> roles) {
        return jpaRepository.findByRoleIn(roles).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findByPhoneIncludingDeleted(String phone) {
        return jpaRepository.findByPhoneIncludingDeleted(phone).map(this::toDomain);
    }

    // Đã sửa tham số từ String thành Enum Role để khớp với Entity của bạn
    @Override
    public List<User> findActiveStaffsByRole(Role role) {
        return jpaRepository.findByRoleAndIsActiveTrue(role).stream()
                .map(this::toDomain)
                .toList(); // Dùng .toList() cho đồng bộ với hàm findByRoleIn ở trên
    }

    // Giữ lại hàm mapper toDomain đầy đủ của bạn, xóa bỏ hàm bị trùng
    private User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;
        return User.builder()
                .id(entity.getId())
                .phone(entity.getPhone())
                .password(entity.getPassword())
                .fullName(entity.getFullName())
                .role(entity.getRole())
                .branchId(entity.getBranchId())
                .points(entity.getPoints())
                .memberTier(entity.getMemberTier())
                .commissionRate(entity.getCommissionRate())
                .isDeleted(entity.isDeleted())
                .isActive(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private UserJpaEntity toJpa(User domain) {
        if (domain == null) return null;
        return UserJpaEntity.builder()
                .id(domain.getId())
                .phone(domain.getPhone())
                .password(domain.getPassword())
                .fullName(domain.getFullName())
                .role(domain.getRole())
                .branchId(domain.getBranchId())
                .points(domain.getPoints())
                .memberTier(domain.getMemberTier())
                .commissionRate(domain.getCommissionRate())
                .isDeleted(domain.isDeleted())
                .isActive(domain.isActive())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}