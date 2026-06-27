package org.example.lv_be.module.users.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.users.application.interfaces.in.IUpdateUserPointsUseCase;
import org.example.lv_be.module.users.domain.entity.Customer;
import org.example.lv_be.module.users.domain.exception.UserDomainException;
import org.example.lv_be.module.users.domain.repository.ICustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserPointsUseCaseImpl implements IUpdateUserPointsUseCase {

    // Sử dụng CustomerRepository thay vì UserRepository
    private final ICustomerRepository customerRepository;

    @Override
    @Transactional
    public void execute(Long userId, int pointsToAdd) {
        // 1. Tìm thông tin khách hàng từ bảng customers dựa vào userId
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new UserDomainException("Không tìm thấy thông tin ví điểm của khách hàng này!"));

        // 2. Tận dụng Rich Domain Model: Gọi hàm để thực thể tự cộng điểm và xét thăng hạng
        customer.addRewardPoints(pointsToAdd);

        // 3. Lưu lại trạng thái mới xuống Database
        customerRepository.save(customer);
    }
}