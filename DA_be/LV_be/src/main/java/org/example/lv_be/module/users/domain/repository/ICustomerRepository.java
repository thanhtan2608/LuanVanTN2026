package org.example.lv_be.module.users.domain.repository;

import org.example.lv_be.module.users.domain.entity.Customer;

import java.util.Optional;

public interface ICustomerRepository {
    Customer save(Customer customer);

    // Tìm thông tin khách hàng dựa trên ID của bảng Users
    Optional<Customer> findByUserId(Long userId);
}