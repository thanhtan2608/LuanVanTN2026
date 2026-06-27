package org.example.lv_be.module.users.infrastructure.database;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.users.domain.entity.Customer;
import org.example.lv_be.module.users.domain.repository.ICustomerRepository;
import org.example.lv_be.module.users.infrastructure.database.entity.CustomerJpaEntity;
import org.example.lv_be.module.users.infrastructure.database.repository.CustomerSpringJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements ICustomerRepository {

    private final CustomerSpringJpaRepository springRepository;

    @Override
    public Customer save(Customer customer) {
        CustomerJpaEntity entity = CustomerJpaEntity.builder()
                .userId(customer.getUserId())
                .points(customer.getPoints())
                .memberTier(customer.getMemberTier())
                // Lưu ý: Thuộc tính user (UserJpaEntity) sẽ tự động liên kết nhờ @MapsId
                .build();

        CustomerJpaEntity savedEntity = springRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Customer> findByUserId(Long userId) {
        return springRepository.findById(userId).map(this::toDomain);
    }

    private Customer toDomain(CustomerJpaEntity entity) {
        return Customer.builder()
                .userId(entity.getUserId())
                .points(entity.getPoints())
                .memberTier(entity.getMemberTier())
                .build();
    }
}